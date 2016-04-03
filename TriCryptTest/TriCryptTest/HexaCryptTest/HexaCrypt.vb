Namespace HexaCrypt

    ''' <summary>
    ''' UTF-8コーディングの文字を、6進法の暗号化された数列に変換する、
    ''' "HexaCrypt"法の暗号化・復号化を実行するクラス。
    ''' </summary>
    ''' <remarks>このあたりに存在する各種コメントなども、完成後は削除予定</remarks>
    Public Class HexaCrypt

        Private hexalNumber As HexalNumber

        ''' <summary>
        ''' コンストラクタで行う処理は特になし
        ''' </summary>
        ''' <remarks></remarks>
        Sub New()
            hexalNumber = New HexalNumber()
        End Sub

        ''' <summary>
        ''' 文字列の暗号化を行うメソッド。
        ''' 受け取った文字列を、それぞれUTF-8のバイト数列に変換する
        ''' </summary>
        ''' <param name="encryptingString">暗号化したい文字列</param>
        ''' <returns>D6進数のユニットに変換され、連結された文字列</returns>
        ''' <remarks>仕様変更。この段階でユニットに格納するのは生のバイト数とし、
        ''' このバイト配列の暗号化はByteArrayEncrypterクラスに一任する:2013/08/14</remarks>
        Public Function encryptStringToD6String(ByVal encryptingString As String) As String

            '返却する6進法文字列
            Dim result As String = ""

            Dim StringAsBytes As Byte() = System.Text.Encoding.UTF8.GetBytes(encryptingString)

            Dim b As Byte

            '現在、何byte目を計算しているかを記録する変数
            Dim i As Integer = 0

            For Each b In StringAsBytes
                '現在の1Byteを、6進数の1ユニットに変換
                hexalNumber.SetDecimalNumber(b)
                '以上の操作でできたD6進数1ユニットを追加
                result = result.Insert(result.Length, hexalNumber.GetHexalNumberAsD6Digits)

                i = i + 1
            Next

            Return result

        End Function

        ''' <summary>
        ''' HexaCrypt法で生成された暗号を、復号化するメソッド。
        ''' </summary>
        ''' <param name="HexaCrypticalString">D6進数を連結してできた文字列</param>
        ''' <returns>復号化された文字列</returns>
        ''' <remarks>仕様変更。このメソッドに持たせる機能は、暗号化されていない状態の
        ''' D6進数の配列とする*2013/08/14</remarks>
        Public Function decryptStringFromD6String(ByVal HexaCrypticalString As String) As String

            'まず、入力された文字の文字数が4の倍数であるかどうかを判定。
            '1ユニットは4つの1~6の数字で成っているので、4の倍数でなければ入力値は不正
            If (HexaCrypticalString.Length Mod 4) <> 0 Then
                Try
                    Throw New Exception("入力された文字列は入力値として不正です")
                Catch ex As Exception
                    MessageBox.Show(ex.Message, "エラー！")
                    Return ""
                End Try
            End If

            '上記のバリデーションを通るなら、本処理を続行。

            '複合化された文字列が何Byteあるかは、入力された6進数のユニット数で計算可能。
            '以降、この値をfor節などで利用
            Dim byteLength As Integer = HexaCrypticalString.Length / 4

            '復号化されたByte数を受け取る配列を宣言
            '(要素数は、byteLength - 1で宣言)
            Dim decryptedBytes(byteLength - 1) As Byte

            '入力された6進数の1ユニットごとを読み取り、それを復号化
            For i As Integer = 0 To byteLength - 1

                '現在のユニットを読み取る
                Dim unit As String = HexaCrypticalString.Substring(i * 4, 4)
                '読み取ったユニットを格納
                Me.hexalNumber.SetHexalNumberFromString(unit)

                '復号化された6進数を、byte数に変換
                Dim b As Byte = hexalNumber.HexalUnitToDecimalByte()

                'この結果をdecryptedBytesに格納
                decryptedBytes(i) = b

            Next

            '上記の処理で得られたbyte配列を、UTF-8エンコーディングで文字列変換
            Dim result As String = System.Text.Encoding.UTF8.GetString(decryptedBytes)

            Return result

        End Function

        ''' <summary>
        ''' 6進法2ユニットぶんのデータを連結し、24bit=3byteの配列に変換する関数
        ''' </summary>
        ''' <param name="former12Bits">連結を行う前半の12bit(0と1からなる文字列であること)</param>
        ''' <param name="latter12Bits">連結を行う後半の12bit(0と1からなる文字列であること)</param>
        ''' <returns>連結され生成された3バイトの配列</returns>
        ''' <remarks></remarks>
        Private Function ConvertTwo12BitsInto3Bytes(ByVal former12Bits As String,
                                                           ByVal latter12Bits As String) As Byte()
            '文字列を連結し24bitの数列を得る
            Dim joinedBits As String = former12Bits & latter12Bits

            '返却用の変数を用意
            Dim compressed3Bytes(2) As Byte

            'joinedBitsを8桁区切りで読み込み、それをバイト数に変換
            For i As Integer = 0 To 2
                Dim target8Bits As String = joinedBits.Substring(i * 8, 8)
                compressed3Bytes(i) = Convert.ToInt32(target8Bits, 2)
            Next

            '以上の処理結果を返却
            Return compressed3Bytes

        End Function

        ''' <summary>
        ''' 3byte=24bitの数列を12bit*2パートに分け、分割した2パートをそれぞれ4桁のD6進数に変換する関数
        ''' </summary>
        ''' <param name="_3Bytes">D6進数に変換したいバイト配列(3Byte)</param>
        ''' <returns>変換されたD6進数2ユニットぶんを表すString配列</returns>
        ''' <remarks></remarks>
        Private Function Convert3BytesTo2HexalNumberUnits(ByVal _3Bytes() As Byte) As String()

            '前処理として入力値が本当に3バイトかを確認。3バイトでないならreturn Nothing
            If _3Bytes.Length <> 3 Then
                Return Nothing
            End If

            '前処理で弾かれなければ本処理を実行。
            'まずは入力された3バイトから、24bitの2進数を作成
            Dim convertedBinary As String = ""

            For i As Integer = 0 To 2
                Dim generatedDigit As String = Convert.ToString(_3Bytes(i), 2)

                'もしこの結果が7桁以下なら、8桁になるまで左端に"0"を追記
                Do While generatedDigit.Length <= 7
                    generatedDigit = generatedDigit.Insert(0, "0")
                Loop

                'この8桁をconvertedBinaryに追加
                convertedBinary = convertedBinary.Insert(convertedBinary.Length, generatedDigit)
            Next

            'このconvertedBinaryを前後12bitごとに読み込む。
            'この12bitのうち3bitずつを10進数として読み込み、返却する4桁の6進数2つを生成
            Dim hexalNumbers() As String = {"", ""}

            For i As Integer = 0 To 1

                'このループで読み取る12bitを取得
                Dim _12Bits As String = convertedBinary.Substring(i * 12, 12)

                'このループの中で、3bitずつ読み取る作業を4回繰り返す
                For j As Integer = 0 To 3

                    Dim _3bits As String = _12Bits.Substring(j * 3, 3)

                    hexalNumbers(i) = hexalNumbers(i).
                                        Insert(hexalNumbers(i).Length, Convert.ToInt32(_3bits, 2).ToString)

                Next

            Next

            'こうして作成されたD6進数2つを返却
            Return hexalNumbers

        End Function

        ''' <summary>
        ''' D6進数のユニットを表す文字列を、等価のバイト配列に変換するメソッド。
        ''' このメソッド内で、ConvertTwo12BitsInto3Bytes()を用いている
        ''' </summary>
        ''' <param name="D6Digits">D6進数のユニットを表す文字列</param>
        ''' <returns>2ユニット→3バイトに変換され、連結されたバイト配列</returns>
        ''' <remarks>なお、入力された文字列について、D6進数のユニット数に換算して奇数個しかない場合、
        ''' 3バイトに変換するうちの後半12ビットには、D6進数で、10進数の255に相当する文字列を用いる</remarks>
        Public Function ConvertD6StringToBytes(ByVal D6Digits As String) As Byte()

            'このD6進数をバイト配列に変換する際、一時格納用に使う配列を作成
            Dim tmpBytes As List(Of Byte) = New List(Of Byte)

            '読み込み回数を計算
            Dim scanningTime As Integer = Math.Ceiling(D6Digits.Length / 8) - 1

            'D6進数2ユニット(8文字)ごとに読み込み
            For i As Integer = 0 To scanningTime

                '前半1ユニット
                Dim formerHexalNumber As String = ""
                '後半1ユニット
                Dim latterHexalNumber As String = ""

                '前半/後半1ユニットごとに、それぞれ検証を行う。
                'もし入力されたバイト配列数が奇数になるなどして、
                '現在ポイントされているユニットから4桁のD6進数が回収できなかった場合、
                'D6進数で255(10進法)に等しい文字列を入れる。
                'UFT-8では254と255は使われないバイト数なので、不必要な文字列が入る危険が無い

                'D6進数を2進数に変換するために用意
                Dim hNumber As HexalNumber = New HexalNumber()

                '前半1ユニットに対する処理
                If (i * 8 + 4) > D6Digits.Length Then
                    'もし変換できる文字列が無いなら、255(10進法)を2進数にしてセット
                    hNumber.SetDecimalNumber(255)
                    formerHexalNumber = hNumber.ConvertTo12Bits()
                Else
                    'D6進数1ユニットをフェッチできるなら、それを2進数の12ビットに変換
                    Dim _1Unit As String = D6Digits.Substring(i * 8, 4)
                    hNumber.SetHexalNumberFromString(_1Unit)
                    formerHexalNumber = hNumber.ConvertTo12Bits(True)
                End If

                '後半1ユニットに対する処理
                If (i * 8 + 8) > D6Digits.Length Then
                    'もし変換できる文字列が無いなら、255(10進法)を2進数にしてセット
                    hNumber.SetDecimalNumber(255)
                    latterHexalNumber = hNumber.ConvertTo12Bits()
                Else
                    'D6進数1ユニットをフェッチできるなら、それを2進数の12ビットに変換
                    Dim _1Unit As String = D6Digits.Substring(i * 8 + 4, 4)
                    hNumber.SetHexalNumberFromString(_1Unit)
                    latterHexalNumber = hNumber.ConvertTo12Bits(True)
                End If

                '前半1ユニット、後半1ユニットを取得して、それを3バイトに変換
                tmpBytes.AddRange(Me.ConvertTwo12BitsInto3Bytes(formerHexalNumber, latterHexalNumber))

            Next

            '以上の処理で得られたバイト配列を返却
            Return tmpBytes.ToArray()

        End Function


        ''' <summary>
        ''' バイト配列を、D6進数のユニットを表す文字列に変換するメソッド。
        ''' このメソッド内で、Convert3BytesTo2HexalNumberUnits()を用いている
        ''' </summary>
        ''' <param name="originalBytes">D6進数に変換したいバイト配列</param>
        ''' <returns>3バイト→2ユニットに変換され、連結されたD6進数の文字列</returns>
        ''' <remarks></remarks>
        Public Function ConvertBytesToD6String(ByVal originalBytes() As Byte) As String

            '入力チェック。もし入力されたバイト配列の個数が3の倍数でない場合、
            '不適切なバイト配列を使ったものとみなしてreturn Nothing
            If (originalBytes.Length Mod 3) <> 0 Then
                Return Nothing
            End If

            'Return Nothingされなければ処理を続行

            '変換されたD6進数を格納する変数
            Dim D6Digits As String = ""

            'D6進数で255を表す文字列を用意
            Dim hNumber As HexalNumber = New HexalNumber()
            hNumber.DecimalByteToHexalUnit(255)
            Dim _255AsD6Digits As String = hNumber.GetHexalNumberAsD6Digits

            'For節の中で、入力されたバイト配列を3バイトずつ読み込んでいく
            For i As Integer = 0 To (originalBytes.Length \ 3 - 1)

                '現在ポイントされているアドレスと、そこから先の合計3バイトを取得
                Dim _3Bytes(2) As Byte
                _3Bytes(0) = originalBytes(i * 3 + 0)
                _3Bytes(1) = originalBytes(i * 3 + 1)
                _3Bytes(2) = originalBytes(i * 3 + 2)

                '続けて、この3バイトを変換した結果を格納する変数を用意
                Dim _2Units(1) As String

                _2Units = Me.Convert3BytesTo2HexalNumberUnits(_3Bytes)

                'こうして得られた2ユニットを、それぞれ確認。
                'もし得られた1ユニットがD6進数で10進数の255を表すなら、その値は無視。
                'そうでないなら、ユニットをD6Digitsに挿入
                If _2Units(0).Equals(_255AsD6Digits) = False Then
                    D6Digits = D6Digits.Insert(D6Digits.Length, _2Units(0))
                End If

                If _2Units(1).Equals(_255AsD6Digits) = False Then
                    D6Digits = D6Digits.Insert(D6Digits.Length, _2Units(1))
                End If

            Next

            'この結果得られた文字列を返却
            Return D6Digits

        End Function

    End Class

End Namespace
