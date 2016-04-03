Namespace HexaCrypt
    ''' <summary>
    ''' 6進法クラス。10進法の1バイト(8bit)の数値を、6進法の4bitとして扱う。
    ''' 以降、このクラス内では、この4bitの6進法の数の塊を「1ユニット」と呼称する。
    ''' (なお、正負の符号は共に考えないので、扱う数値は正の数のみ)
    ''' このクラスの中には、4つのintegerを格納するローカル変数のみがメンバ変数として存在する。
    ''' このメンバ変数は0~5の数字のみを受け付ける。
    ''' アドレスが(0)である数値が最大の桁、(3)である桁が最小の桁、という表し方である。
    ''' (つまり、このローカル変数の配列が、このクラスの表す6進法の数値の各桁の値を管理している)
    ''' 受け取った10進法の数値を6進法にしたり、6進法の数値を逆に10進法に戻したりするのは、メソッドに一任。
    ''' 内部計算では、原則10進法を用いて計算している。
    ''' </summary>
    ''' <remarks>なお、内部的な計算で使う数値は0~5。1~6ではないので注意！
    ''' 以降、0~5の数字を使う表現方法を「6進法」、1~6の数字を使う表現方法を「D6進法」と呼称する
    ''' (D6=6 face Dice=6面体サイコロ、の意)</remarks>
    Public Class HexalNumber

        Private _digits(3) As Integer

        ''' <summary>
        ''' このクラスと同名のプロパティは、1ユニット=4bitの6進法数列を文字列として返却する。
        ''' (返却値はD6進数ではない！　注意！)
        ''' </summary>
        ''' <value></value>
        ''' <returns>このクラスに格納された6進法の1ユニット</returns>
        ''' <remarks></remarks>
        Public ReadOnly Property HexalNumber As String
            Get
                Dim result As String = ""
                For i As Integer = 0 To 3
                    result = result.Insert(result.Length, _digits(i).ToString)
                Next
                Return result
            End Get
        End Property


        Sub New()
            '最初は各行0からスタート
            _digits = {0, 0, 0, 0}
        End Sub

        ''' <summary>
        ''' 10進数の数値を入力し、それを6進数に変換して内部的に保存するセッター
        ''' </summary>
        ''' <param name="decimalNumber">10進法で表されたバイト数(</param>
        ''' <remarks></remarks>
        Public Sub SetDecimalNumber(ByVal decimalNumber As Byte)
            Me.DecimalByteToHexalUnit(decimalNumber)
        End Sub

        ''' <summary>
        ''' 10進法の1バイト(8bit)の数値を、6進法の4ビット=1ユニットの数値に変換する
        ''' </summary>
        ''' <param name="decimalNumber"></param>
        ''' <remarks></remarks>
        Public Sub DecimalByteToHexalUnit(ByVal decimalNumber As Byte)

            '引数をローカル変数に格納
            Dim number As Byte = decimalNumber

            '以下、二重ループで入力された値を4bitの6進数に変換する
            For j As Integer = 3 To 0 Step (-1)
                For i As Integer = 5 To 0 Step (-1)

                    '6のj乗の何倍が、初めて入力された数字の値を上回るかを計算する。
                    If number >= (6 ^ j) * i Then

                        'この節内に入ったなら、if節の右辺で左辺を引き算し、
                        'そして現在のiの値を4桁目の数字として採用後、ループを脱出
                        number = number - (6 ^ j) * i
                        _digits(3 - j) = i
                        Exit For

                    End If

                Next 'iのループ終了
            Next 'jのループ終了

        End Sub

        ''' <summary>
        ''' このインスタンスに現在記録されている6進法の4ビット=1ユニットの数値を、
        ''' 10進法の1バイト(8bit)の数値に変換して返却する
        ''' </summary>
        ''' <remarks></remarks>
        Public Function HexalUnitToDecimalByte() As Byte

            '返却用の値
            Dim b As Byte = 0

            For i As Integer = 3 To 0 Step (-1)

                b = b + _digits(3 - i) * (6 ^ i)

            Next

            Return b
        End Function

        ''' <summary>
        ''' このクラスのメンバ変数に、外部からD6進数の数を入力してセットするメソッド。
        ''' ただしここで入力可能なのは4桁の整数のみで、かつそれらに使える値は1~6のみという制限がある
        ''' </summary>
        ''' <param name="HexalUnit">セットしたいD6進数の数値</param>
        ''' <remarks></remarks>
        Public Sub SetHexalNumberFromString(ByVal HexalUnit As String)

            '本処理を始める前に、入力値についてバリデーション。この結果がfalseなら、
            '例外を投げる
            If (Me.ValidateHexalNumber(HexalUnit) = False) Then
                Try
                    Throw New Exception("入力された文字列は入力値として不正です")
                Catch ex As Exception
                    MessageBox.Show(ex.Message, "エラー！")
                    Return
                End Try
            End If

            '上記のバリデーションを通ったなら、入力された値を_digits()に各々格納。
            'この時点で、-1を引っ掛けて、「内部計算は0~5のみで行う」のルールに沿う形でコーディング
            For i As Integer = 0 To 3
                _digits(i) = CInt(HexalUnit.Substring(i, 1)) - 1
            Next

        End Sub

        ''' <summary>
        ''' 現在このクラスに登録された4bitの6進数について、
        ''' 各桁の数字に1を足し、D6進数として変換した結果を文字列として返却する。
        ''' (例:0123→1234など)
        ''' </summary>
        ''' <returns></returns>
        ''' <remarks></remarks>
        Public Function GetHexalNumberAsD6Digits() As String

            Dim result As String = ""

            For i As Integer = 0 To 3
                result = result.Insert(result.Length, (Me._digits(i) + 1).ToString)
            Next

            Return result

        End Function

        ''' <summary>
        ''' このユニット内の数字を、指定された数だけ左にシフトさせる。
        ''' 左端にかかった数字は、右端に移動する。
        ''' たとえば"1234"というユニットについて、1つ左シフトした場合、その結果は
        ''' "2341"となる。
        ''' </summary>
        ''' <param name="ShiftingNumber">左シフトを行わせたい桁数</param>
        ''' <remarks>引数は0~3を指定することを推奨</remarks>
        Public Sub ShiftToLeft(ByVal ShiftingNumber As Integer)

            'まず剰余の計算を行い、指定されたシフト数では、何回左シフトを行うかを計算。
            '6進法の1ユニットは4桁なので、実質的には0~3回のシフトで、あらゆるシフトパターンを網羅できる。
            'たとえば、「4回の左シフト」を行うと、結局0回シフト=シフトしない、と同じとなる。
            '"1234"→(左シフト)→"2341"→(左シフト)→"3412"→(左シフト)→"4123"→(左シフト)→"1234"……(以下ループ)
            Dim FinalShiftNumber As Integer = ShiftingNumber Mod 4

            'これにより算出された回数だけ、左シフトを行う
            For i As Integer = 0 To FinalShiftNumber

                '左端の数字だけ、一時変数に格納
                Dim temp As Integer = _digits(0)
                _digits(0) = _digits(1)
                _digits(1) = _digits(2)
                _digits(2) = _digits(3)
                _digits(3) = temp

            Next

        End Sub

        ''' <summary>
        ''' そのユニット内の数字を、指定された数だけ右にシフトさせる。
        ''' 右端にかかった数字は、左端に移動する。
        ''' たとえば"1234"というユニットについて、1つ右シフトした場合、その結果は
        ''' "4123"となる。
        ''' </summary>
        ''' <param name="ShiftingNumber">右シフトを行わせたい桁数</param>
        ''' <remarks>引数は0~3を指定することを推奨</remarks>
        Public Sub ShiftToRight(ByVal ShiftingNumber As Integer)

            'まず剰余の計算を行い、指定されたシフト数では、何回右シフトを行うかを計算。
            '6進法の1ユニットは4桁なので、実質的には0~3回のシフトで、あらゆるシフトパターンを網羅できる。
            'たとえば、「4回の右シフト」を行うと、結局0回シフト=シフトしない、と同じとなる。
            '"1234"→(右シフト)→"4123"→(右シフト)→"3412"→(右シフト)→"2341"→(右シフト)→"1234"……(以下ループ)
            Dim FinalShiftNumber As Integer = ShiftingNumber Mod 4

            'これにより算出された回数だけ、左シフトを行う
            For i As Integer = 0 To FinalShiftNumber

                '右端の数字だけ、一時変数に格納
                Dim temp As Integer = _digits(3)
                _digits(3) = _digits(2)
                _digits(2) = _digits(1)
                _digits(1) = _digits(0)
                _digits(0) = temp

            Next

        End Sub

        ''' <summary>
        ''' 6進法の1ユニットにおける、最初の1bit目の数値を増加させるメソッド。
        ''' HexaCrypt法で暗号化された1byteの整数は、6進法の1ユニット目が必ず1か2になるが、
        ''' この性質がクラッカーに対するヒントになる危険性がある。
        ''' そこで、1ユニット目の数字に0~5の数字を追加し、ハッカーに対する「迷彩」をかける目的で、
        ''' このメソッドを作成する
        ''' </summary>
        ''' <param name="AddingNumber"></param>
        ''' <remarks>引数は0~5を指定することを推奨</remarks>
        Public Sub IncreaseFirstBitValue(ByVal AddingNumber)

            'まず剰余の計算を行い、最終的にいくつの数値を加えるかを決定。
            '0~5のいずれかを指定
            Dim FinalAddingNumber As Integer = AddingNumber Mod 6

            '最初の1桁目の数値に、この値を追加
            _digits(0) = _digits(0) + FinalAddingNumber

            'もしこの結果、値が5を超過した場合、-6して計算結果を0~5に収める
            If _digits(0) > 5 Then
                _digits(0) = _digits(0) - 6
            End If

        End Sub

        ''' <summary>
        ''' 6進法の1ユニットにおける、最初の1bit目の数値を減少させるメソッド。
        ''' HexaCrypt法で暗号化された1byteの整数は、6進法の1ユニット目が必ず1か2になるが、
        ''' この性質がクラッカーに対するヒントになる危険性がある。
        ''' そこで、1ユニット目の数字に0~5の数字を追加し、ハッカーに対する「迷彩」をかける目的で、
        ''' このメソッドを作成する。
        ''' なお、この作業で「オーバーフロー」が起こった場合は、数字は最大値の5に戻る
        ''' </summary>
        ''' <param name="AddingNumber"></param>
        ''' <remarks>引数は0~5を指定することを推奨</remarks>
        Public Sub DecreaseFirstBitValue(ByVal AddingNumber)

            'まず剰余の計算を行い、最終的にいくつの数値を加えるかを決定。
            '0~5のいずれかを指定
            Dim FinalAddingNumber As Integer = AddingNumber Mod 6

            '最初の1桁目の数値を、この値ぶん減少させる
            _digits(0) = _digits(0) - FinalAddingNumber

            'もしこの結果、値が0を下回ったした場合、+6して計算結果を0~5に収める
            If _digits(0) < 0 Then
                _digits(0) = _digits(0) + 6
            End If

        End Sub

        ''' <summary>
        ''' D6進数のバリデーション。
        ''' もし入力値が以下の2条件を共に満たさない場合、falseが返る。
        ''' 1.入力値の文字数は4文字である
        ''' 2.入力された文字はすべて1~6の数字のみである
        ''' </summary>
        ''' <param name="HexalUnit">検証を行いたい文字列</param>
        ''' <returns>検証結果</returns>
        ''' <remarks></remarks>
        Private Function ValidateHexalNumber(HexalUnit As String) As Boolean

            'もし文字数が4文字でない場合、return false
            If HexalUnit.Length <> 4 Then
                Return False
            End If

            '以下、入力された文字列がすべて1~6であるかどうかの検証。
            '一つでもこれ以外の文字列があったら即座にreturn false
            For i As Integer = 0 To HexalUnit.Length - 1

                If (HexalUnit.Substring(i, 1) = "1") Or _
                   (HexalUnit.Substring(i, 1) = "2") Or _
                   (HexalUnit.Substring(i, 1) = "3") Or _
                   (HexalUnit.Substring(i, 1) = "4") Or _
                   (HexalUnit.Substring(i, 1) = "5") Or _
                   (HexalUnit.Substring(i, 1) = "6") Then
                    Continue For
                Else
                    Return False
                End If

            Next

            'ここまで処理が届いたなら、入力値に問題はないのでreturn true
            Return True

        End Function

        ''' <summary>
        ''' 現在このクラスの保持している6進数D6進数に変換し、12bitの数列として返却する関数。
        ''' そもそも6進法orD6進数の1桁は、冗長性を無くせば2進法の3bitで表現できる。(3ビットなら0~7の数字を表現可)
        ''' そしてこのクラスに格納できる6進法は4桁より、3bit×4=12bitで表現できる。
        ''' なお、返却される3*4bitは、原則1~6を表す3bitを返却する。
        ''' 0~5を表す3bitを返却してほしいときは、引数にfalseを指定すること
        ''' (例:このクラスに"0123"という数値が格納されていた場合、これのD6進数表現である"1234"を内部的に生成。
        ''' そして1を表す"001"、2を表す"010"、3を表す"011"、4を表す"100"が連結された12bit、
        ''' "001010011100"が返却される。
        ''' 引数にfalseが指定されていた場合、"0123"を表す12bit、"000001010011"が返却される
        ''' </summary>
        ''' <returns>現在保持されたD6進数1ユニットぶんを表す12bitの文字列</returns>
        ''' <remarks></remarks>
        Public Function ConvertTo12Bits(Optional ByVal uses1to6 As Boolean = True) As String

            Dim result As String = ""

            If uses1to6 = True Then
                '1~6の数字を使う場合、以下の処理を行う
                For i As Integer = 0 To 3
                    Dim adding3Bits As String = Convert.ToString((Me._digits(i) + 1), 2)

                    'もしこの時点でadding3Bitsが3桁でないなら、3桁になるまで左端に0を追加
                    Do While adding3Bits.Length <= 2
                        adding3Bits = adding3Bits.Insert(0, "0")
                    Loop

                    result = result.Insert(result.Length, adding3Bits)
                Next
            Else
                '0~5の数字を使う場合、以下の処理を行う
                For i As Integer = 0 To 3
                    Dim adding3Bits As String = Convert.ToString(Me._digits(i), 2)

                    'もしこの時点でadding3Bitsが3桁でないなら、3桁になるまで左端に0を追加
                    Do While adding3Bits.Length <= 2
                        adding3Bits = adding3Bits.Insert(0, "0")
                    Loop

                    result = result.Insert(result.Length, adding3Bits)
                Next
            End If

            Return result

        End Function

    End Class

End Namespace
