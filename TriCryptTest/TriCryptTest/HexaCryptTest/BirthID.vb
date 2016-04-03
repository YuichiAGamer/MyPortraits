''' <summary>
''' 各ディヴァイスに一意的に与えられるID、BirthID(バースID)の諸機能を担当するクラス。
''' BirthidはDateEncrypterで生成される15~18桁の数字(以下これを『ディヴァイスカードのシリアルID』と呼称)と、
''' 作成者が打ち込む任意のパスワードから生成されるidである。
''' シリアルIDとパスワードを、それぞれ10バイトの数列に変換後、それらのバイト配列にトライシャッフルを行う。
''' (三重シャッフル=ディールシャッフル、フラップシャッフル、リフルシャッフルを特定の順序で行うシャッフル操作)
''' このトライシャッフル後の10byteの配列を、各アドレスごとにそれぞれxor演算を行うことで導き出される。
''' バースIDにに対しては、トライシャッフル後のパスワードでxor演算を行い、逆トライシャッフルをかけると、
''' DateEncrypterの素数積が戻ってくる。
''' その素数積がディヴァイス・カードや.oddファイルに記載された素数積と同じであれば、
''' そのパスワードは正しい≒パスワードを知っている、そのディヴァイスの作成者、と同定できる。
''' 上記のような機能を果たす目的で、birthIDは作られる。
''' </summary>
''' <remarks></remarks>
Public Class BirthID

    'コンストラクタ
    Sub New()

    End Sub

    ''' <summary>
    ''' 入力されたシリアルIDとパスワードから、バースIDを生成する関数
    ''' </summary>
    ''' <param name="serialID">シリアルID</param>
    ''' <param name="password">パスワード</param>
    ''' <returns>バースID</returns>
    ''' <remarks></remarks>
    Public Function GenerateBirthID(ByVal serialID As Decimal, ByVal password As String) As String

        'まず、serialIDを20桁きっかりに整形
        Dim serialIDWith20Degree As String = MakeInputAs20Degree(serialID)

        'この結果を、10バイトの配列に変換
        Dim _10byteFromSerialID As Byte() = Split20DegreeDigitTo10Byte(serialIDWith20Degree)

        '入力されたパスワードも、10バイトぶんに調整
        Dim _10byteFromPassword As Byte() = MakePasswordAs10ByteArray(password)

        '続けて、これらのバイト配列の合計値をキーナンバーとして、トライシャッフルを実行
        _10byteFromSerialID = ExecuteTriShuffle(_10byteFromSerialID)
        _10byteFromPassword = ExecuteTriShuffle(_10byteFromPassword)

        'この配列2つを、同じ要素数のバイト配列ごとに、xor演算
        Dim xorOperatedArray As Byte() = DoXorOperationTo2Arrays(_10byteFromSerialID, _10byteFromPassword)

        '得られたxorOperatedArrayを、20桁表示の16進法の連結結果として返却
        Return GetSeparetedByteString(xorOperatedArray)

    End Function

    ''' <summary>
    ''' バースIDとパスワードを組み合わせて、シリアルIDを復元する関数。
    ''' そのバースIDとパスワードの組み合わせが正しければ、正しいシリアルIDが復元される
    ''' </summary>
    ''' <param name="birthID">バースID</param>
    ''' <param name="password">パスワード</param>
    ''' <returns></returns>
    ''' <remarks></remarks>
    Public Function DecryptSerialIDFromBirthIDAndPassword(ByVal birthID As String, ByVal password As String) As Decimal

        'バースIDからセパレータを取り除き、10バイトの配列を作る
        Dim DecryptedByteArrayOfBirthID() As Byte = DecryptByteArrayOfBirthID(birthID)

        'パスワードは、上記の配列とxor演算する前に、10バイトに整形しトライシャッフル
        Dim _10byteFromPassword() As Byte = MakePasswordAs10ByteArray(password)
        _10byteFromPassword = ExecuteTriShuffle(_10byteFromPassword)

        'この配列2つを、同じ要素数のバイト配列ごとに、xor演算
        Dim xorOperatedArray() As Byte = DoXorOperationTo2Arrays(DecryptedByteArrayOfBirthID, _10byteFromPassword)

        '得られたバイト配列に逆トライシャッフルをかけ、正しい並び順を復元
        Dim reorderedArray() As Byte = ExecuteReversedTriShuffle(xorOperatedArray)

        'この配列内の合計20桁の数字を連結し、それを返却

        Return GetDecimalFromByteArray(reorderedArray)

    End Function

    ''' <summary>
    ''' 入力された整数が20桁未満である場合、20桁になるよう
    ''' 数字の左端に"0"を追加し、返却する関数
    ''' </summary>
    ''' <param name="input">20桁未満の整数</param>
    ''' <returns>20桁になった整数の文字列</returns>
    ''' <remarks></remarks>
    Public Function MakeInputAs20Degree(ByVal input As Decimal) As String

        Dim output As String = input.ToString

        Do While output.Length < 20
            output = output.Insert(0, "0")
        Loop

        Return output

    End Function

    ''' <summary>
    ''' 20桁の整数の文字列を10byteの配列にして返却する関数
    ''' </summary>
    ''' <param name="input">20桁の10進法の整数</param>
    ''' <returns>10byteの配列</returns>
    ''' <remarks></remarks>
    Public Function Split20DegreeDigitTo10Byte(ByVal input As String) As Byte()

        '入力値が20桁でないなら、強制リターン
        If input.Length <> 20 Then
            Return Nothing
        End If

        '強制リターンがない場合、以下の処理を続行

        'まず、入力値を2桁ごとのユニットに切り分ける
        Dim splitUnits As List(Of String) = New List(Of String)

        For i As Integer = 0 To 9
            splitUnits.Add(input.Substring(i * 2, 2))
        Next

        'こうして取得された各ユニットを、16進数として解釈しByteとして読み込む
        '(例:入力値が"1234567890"なら、"12"→18(10進法)、"34"→52(10進法)、……"90"→144(10進法)、となる)
        Dim bytes As List(Of Byte) = New List(Of Byte)

        For i As Integer = 0 To (splitUnits.Count - 1)
            bytes.Add(Convert.ToInt32(splitUnits(i), 16).ToString)
        Next

        'この結果を配列として返却
        Return bytes.ToArray

    End Function

    ''' <summary>
    ''' 入力されたパスワードをUTF-8文字コーディングによる10バイトの配列に変換する関数。
    ''' もし入力された文字列をバイト配列に変換して10バイト未満なら、直後に"255"の要素をセパレータとして追加し、
    ''' そして10バイトになるまで入力値の繰り返し→セパレータ追加→……という処理を行う。
    ''' (例:入力値が"ABC"だったとする。文字列"ABC"はUTF-8のバイト配列に換算すると41,42,43。
    ''' ここにセパレータの255を挟みながら10バイトの配列にするので、最終的な結果は
    ''' 41,42,43,255,41,42,43,255,41,42となる)
    ''' 
    ''' </summary>
    ''' <param name="password"></param>
    ''' <returns></returns>
    ''' <remarks>UTF-8では0xFE(=254(10))、0xFF(=255(10))というバイト数は使わないので、
    ''' 255という数字をセパレータに用いてもOK。
    ''' 入力されるパスワードの想定は「10文字以下の半角記号と英数字」だが、
    ''' 理論上漢字の文字列をパスワードとして使用することも可能</remarks>
    Public Function MakePasswordAs10ByteArray(password As String) As Byte()

        '入力された文字列を、UTF-8コーディングによるバイト配列に変換
        Dim convertedArray() As Byte = System.Text.Encoding.UTF8.GetBytes(password)



        '最終的に返却するバイト配列の入れ物
        Dim _10ByteListAsResult As List(Of Byte) = New List(Of Byte)

        'この結果、もし長さが10byte未満である場合、10byteになるまで、
        'セパレータである"255"の要素を噛ませながら、バイト配列を繰り返し。
        'もし10byteを超えているなら、最初の5byteと最後の5byteを抜き出し、
        'それらを連結して10byteの配列とする。
        '10byteぴったりなら、convertedArrayをそのままセット
        If convertedArray.Length < 10 Then

            '現在、convertedArrayのどこを指しているかを指すアドレス
            Dim arrayAddress As Integer = 0

            'generated10ByteListがの長さが10きっかりになるまで繰り返し
            Do While _10ByteListAsResult.Count < 10

                '現在のarrayAddressが指しているアドレスにあるバイト配列を追加
                _10ByteListAsResult.Add(convertedArray(arrayAddress))

                'arrayAddressを+1して次のループに備える
                arrayAddress = arrayAddress + 1

                'この時点でarrayAddressが、convertedArrayの長さ以上なら、
                'セパレータである"255"の要素をgenerated10ByteListに挿入し、
                'arrayAddressを0にリセット
                If arrayAddress >= convertedArray.Length Then
                    _10ByteListAsResult.Add(255)
                    arrayAddress = 0
                End If

            Loop

        ElseIf convertedArray.Length > 10 Then

            'generatedArrayの先頭5バイトをgenerated10byteListにセット
            For i As Integer = 0 To 4
                _10ByteListAsResult.Add(convertedArray(i))
            Next

            '続けて、generatedArrayの後尾5バイトをgenerated10byteListにセット
            For i As Integer = (convertedArray.Length - 5) To (convertedArray.Length - 1)
                _10ByteListAsResult.Add(convertedArray(i))
            Next

        Else

            'この節にたどり着いたということは、パスワードが10バイトぴったりなので、
            'convertedArrayをそのままセット
            _10ByteListAsResult.AddRange(convertedArray)

        End If

        '以上の結果を配列に変換して返却
        Return _10ByteListAsResult.ToArray

    End Function

    ''' <summary>
    ''' バイト配列をシャッフルし、対クラッカー撹乱を行う「トライシャッフル」を実行。
    ''' トライシャッフルは、ディールシャッフル、フラップシャッフル、リフルシャッフルの3つのシャッフルから成る。
    ''' これらを実行する順序、および各シャッフルのオプションはkeyNumberによって決定される。
    ''' keyNumberは、入力されたバイト配列の合計値で決まる。
    ''' バイト配列の並び順がどのように変わっても、その合計値は変わらないことを利用し、
    ''' そのシャッフルパターンを一意的に同定する。
    ''' </summary>
    ''' <param name="targetArray">トライシャッフルを行いたいバイト配列</param>
    ''' <returns>バイト配列にトライシャッフルを行った結果</returns>
    ''' <remarks></remarks>
    Private Function ExecuteTriShuffle(ByVal targetArray As Byte()) As Byte()

        'まず、キーナンバーとして使う、バイト配列の合計値を計算
        Dim keyNumber As Decimal = GetSumOfByteArray(targetArray)

        '計算結果として返却する数列を用意
        Dim resultArray As Byte() = targetArray

        'このキーナンバーの剰余により、3種のシャッフルをどの順番で行うかを決定
        If (keyNumber Mod 3) = 0 Then
            'Modが0なら、ディールシャッフル→フラップシャッフル→リフルシャッフルの順番
            resultArray = DoDealShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoFlapShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoRiffleShuffleToByteArray(resultArray, keyNumber)

        ElseIf (keyNumber Mod 3) = 1 Then
            'Modが1なら、フラップシャッフル→リフルシャッフル→ディールシャッフルの順番
            resultArray = DoFlapShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoRiffleShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoDealShuffleToByteArray(resultArray, keyNumber)
        Else
            'Modが2なら、リフルシャッフル→ディールシャッフル→フラップシャッフルの順番
            resultArray = DoRiffleShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoDealShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoFlapShuffleToByteArray(resultArray, keyNumber)

        End If

        'このトライシャッフルの結果を返却
        Return resultArray

    End Function

    ''' <summary>
    ''' 入力されたバイト配列について、各要素の合計値を返却する関数
    ''' </summary>
    ''' <param name="targetArray">合計を求めたいバイト配列</param>
    ''' <returns>バイト配列の合計値</returns>
    ''' <remarks></remarks>
    Private Function GetSumOfByteArray(ByVal targetArray() As Byte) As Decimal

        Dim sum As Decimal = 0

        For i As Integer = 0 To targetArray.Length - 1
            sum = sum + targetArray(i)
        Next

        Return sum

    End Function

    ''' <summary>
    ''' 入力された二つのバイト配列について、同じアドレスの要素同士を抜き出し、
    ''' xor演算した結果を返却する関数
    ''' </summary>
    ''' <param name="targetByte1">xor演算の対象となる配列1</param>
    ''' <param name="targetByte2">xor演算の対象となる配列2</param>
    ''' <returns>xor演算された配列</returns>
    ''' <remarks></remarks>
    Private Function DoXorOperationTo2Arrays(targetByte1 As Byte(), targetByte2 As Byte()) As Byte()

        '事前チェック。もし二つの数列の長さが同じでないなら、Nothingを強制リターン
        If targetByte1.Length <> targetByte2.Length Then
            Return Nothing
        End If

        '事前チェックを通ったなら、返却値を用意
        Dim resultList As List(Of Byte) = New List(Of Byte)

        '各値について、xor演算
        For i As Integer = 0 To (targetByte1.Length - 1)

            resultList.Add(targetByte1(i) Xor targetByte2(i))

        Next

        'この結果を返却
        Return resultList.ToArray

    End Function

    ''' <summary>
    ''' 入力されたバイト配列を文字列に変換し、すべてを連結。
    ''' 更に、その連結された文字列について、5文字ごとにセパレータの"-"を挟んで返却する関数
    ''' </summary>
    ''' <param name="targetArray">文字列化したいバイト配列</param>
    ''' <returns>セパレータを挟まれた16進数の文字列</returns>
    ''' <remarks></remarks>
    Private Function GetSeparetedByteString(targetArray As Byte()) As String

        '返却用の文字列を作成
        Dim resultString As String = ""

        '入力された文字列を次々加えてゆく
        For i As Integer = 0 To targetArray.Length - 1
            Dim addingStr As String = Convert.ToString(targetArray(i), 16)

            'もしaddingStrが16進数のF(=15(10))以下なら、前に0を追加
            If addingStr.Length = 1 Then
                addingStr = addingStr.Insert(0, "0")
            End If

            resultString = resultString.Insert(resultString.Length, addingStr)
        Next

        'こうして得られた16進法の文字列について、英字部分を大文字化
        resultString = resultString.ToUpper

        '更に、5文字ごとにセパレータの"-"を追加。
        'resultStringを、一旦5桁ごとに区切る
        Dim splitStringList As List(Of String) = New List(Of String)

        For i As Integer = 0 To (resultString.Length \ 5 - 1)
            Dim initial As Integer = i * 5 'スキャン開始位置
            Dim terminal As Integer = (i + 1) * 5 - 1 'スキャン終了位置
            Dim scanAmount As Integer = 5

            'もし最後の文字のインデックスが、resultStringの文字列長-1を上回るようなら、
            'スキャン幅をそのぶん縮める
            If terminal > (resultString.Length - 1) Then
                scanAmount = scanAmount - (terminal - (resultString.Length - 1))
            End If

            splitStringList.Add(resultString.Substring(initial, scanAmount))

        Next

        resultString = ""

        'こうして分割された5文字のユニットを、"-"を挟みつつ追加
        For i As Integer = 0 To (splitStringList.Count - 1)
            resultString = resultString.Insert(resultString.Length, splitStringList(i))
            resultString = resultString.Insert(resultString.Length, "-")
        Next

        '最後尾に"-"が残ったら、それを除去
        If resultString.Substring((resultString.Length - 1), 1) = "-" Then
            resultString = resultString.Remove((resultString.Length - 1), 1)
        End If

        'この結果を返却
        Return resultString

    End Function

    ''' <summary>
    ''' 入力されたバースIDから、セパレーターの"-"を取り除き、20桁の16進数を取得。
    ''' これらをそれぞれ2桁ごとのペアにし、それを1バイトの数字に変換して、10バイトの配列を得る
    ''' </summary>
    ''' <param name="birthID">バースID</param>
    ''' <returns>10バイトの配列</returns>
    ''' <remarks></remarks>
    Private Function DecryptByteArrayOfBirthID(ByVal birthID As String) As Byte()

        'バースIDからセパレーターを取り除き、変換の準備
        Dim originalID As String = birthID.Replace("-", "")

        'バイト配列返却用のリストを作成
        Dim resultByteList As List(Of Byte) = New List(Of Byte)

        '2桁ずつ読み込んで、バイト数に変換
        For i As Integer = 0 To (originalID.Length - 1) Step 2

            Dim byteStr As String = originalID.Substring(i, 2)

            resultByteList.Add(Convert.ToByte(byteStr, 16))

        Next

        '以上の結果を返却
        Return resultByteList.ToArray

    End Function

    ''' <summary>
    ''' 「トライシャッフル」を逆順で実行するメソッド。
    ''' 逆ディールシャッフル、逆フラップシャッフル、逆リフルシャッフルの3つのシャッフルを、
    ''' keyNumberをもって、逆順で実行する
    ''' </summary>
    ''' <param name="targetArray">逆トライシャッフルを行いたいバイト配列</param>
    ''' <returns>トライシャッフルから復元されたバイト配列</returns>
    ''' <remarks></remarks>
    Private Function ExecuteReversedTriShuffle(ByVal targetArray() As Byte) As Byte()

        'まず、キーナンバーとして使う、バイト配列の合計値を計算
        Dim keyNumber As Decimal = GetSumOfByteArray(targetArray)

        '計算結果として返却する数列を用意
        Dim resultArray As Byte() = targetArray

        'このキーナンバーの剰余により、3種のシャッフルをどの順番で行うかを決定
        If (keyNumber Mod 3) = 0 Then
            'Modが0なら、逆リフルシャッフル→逆フラップシャッフル→逆ディールシャッフルの順番
            resultArray = DoReversedRiffleShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoReversedFlapShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoReversedDealShuffleToByteArray(resultArray, keyNumber)

        ElseIf (keyNumber Mod 3) = 1 Then
            'Modが1なら、逆ディールシャッフル→逆リフルシャッフル→逆フラップシャッフルの順番
            resultArray = DoReversedDealShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoReversedRiffleShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoReversedFlapShuffleToByteArray(resultArray, keyNumber)
        Else
            'Modが2なら、逆フラップシャッフル→逆ディールシャッフル→逆リフルシャッフルの順番
            resultArray = DoReversedFlapShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoReversedDealShuffleToByteArray(resultArray, keyNumber)
            resultArray = DoReversedRiffleShuffleToByteArray(resultArray, keyNumber)

        End If

        'このトライシャッフルの結果を返却
        Return resultArray

    End Function

    ''' <summary>
    ''' 入力されたバイト配列を16進数に変換し、その結果を文字列として連結。
    ''' 最後にdecimal型として返却するメソッド
    ''' </summary>
    ''' <param name="targetArray">数値の生成元となる配列</param>
    ''' <returns>連結された数値</returns>
    ''' <remarks></remarks>
    Private Function GetDecimalFromByteArray(targetArray As Byte()) As Decimal

        '内部的には、まず文字列として数値を扱う
        Dim resultString As String = ""

        For i As Integer = 0 To (targetArray.Length - 1)

            '現在のアドレスにあるバイト数を取得
            Dim targetByte As Byte = targetArray(i)

            'これを16進数として読み込む。復号化が正しければ、この時点でA~Fは使われない
            Dim addingStr As String = Convert.ToString(targetByte, 16)

            'もし得られた値が1桁なら、2桁になるまで左端に0を追加
            If addingStr.Length < 2 Then
                addingStr = addingStr.Insert(0, "0")
            End If

            'この数値をresultStringに追加
            resultString = resultString.Insert(resultString.Length, addingStr)

        Next

        'この計算結果を、decimalとして返却
        Return CDec(resultString)

    End Function

    ''' <summary>
    ''' 一つ目の引数となったバイト配列に対し、「ディールシャッフル」を行う。
    ''' バイト配列をカードの束に見立て、バイト配列をいくつかの束に配り分ける。
    ''' 最後にそれをまとめ直すことで、シャッフルは終わる。
    ''' この際、「束」をいくつにするかは、入力されたkeyNumberを5で割った剰余+2で決める。
    ''' (最低でも束は2つ作るため)
    ''' </summary>
    ''' <param name="targetByteArray">シャッフルを行いたい対象のバイト配列</param>
    ''' <param name="keyNumber">束をいくつ作るかを決めるキー番号</param>
    ''' <returns>ディールシャッフルが行われたバイト配列</returns>
    ''' <remarks></remarks>
    Public Function DoDealShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

        '###################
        'Phase1.「束」の用意
        '###################

        'まずはkeyNumberを5で割った剰余+2で、束の数を決める
        Dim bunchAmount As Integer = keyNumber Mod 5 + 2

        '上記の数だけ、「束」となるbyte配列のコレクションを作成。
        'いわゆる二次元配列になるので、実装がややトリッキー。注意！
        Dim byteBunch As List(Of List(Of Byte)) = New List(Of List(Of Byte))

        'Maxの6束ぶん、byteBunch内にList(Of Byte)を追加
        For i As Integer = 0 To 5
            byteBunch.Add(New List(Of Byte))
        Next

        '###########################################
        'Phase2.入力された配列を、各「束」に割り振る
        '###########################################

        '「配り終えた」配列の数
        Dim dealtAmount As Integer = 0

        '現在数字を「配る」対象となる「束」のアドレス
        Dim bunchAddress As Integer = 0

        '入力されたすべてのbyte配列を「束」に分け終えるまで、二次元配列に数値を割り振っていく
        Do While (dealtAmount < targetByteArray.Length)

            '現在対象となっているカードを、アドレスの参照先の束に入れる
            byteBunch(bunchAddress).Add(targetByteArray(dealtAmount))

            'カードが配られたことにより、dealtAmountをカウントアップ
            dealtAmount = dealtAmount + 1

            'アドレスを1つ先のものにする
            bunchAddress = bunchAddress + 1

            'この時点でbunchAddressが使っている束の数以上なら、0にリセット
            If bunchAddress >= bunchAmount Then
                bunchAddress = 0
            End If

        Loop '数字割り振りのループ終了

        '#############################################
        'Phase3.割り振られた各「束」をまとめ直して返却
        '#############################################

        'こうして「配られ」たbyteを、今度は一つの配列にまとめ直す
        Dim shuffledResult As List(Of Byte) = New List(Of Byte)

        '配列のまとめ直し作業は、二重ネストで行う
        For i As Integer = 0 To (bunchAmount - 1)
            For j As Integer = 0 To (byteBunch(i).Count - 1)
                shuffledResult.Add(byteBunch(i)(j))
            Next
        Next

        '以上の結果を、配列に変換して返却
        Return shuffledResult.ToArray

    End Function

    ''' <summary>
    ''' DoDealShuffleToByteArrayメソッドでシャッフルされたバイト配列を、
    ''' 再び元の並びに復元する関数。行っている操作は、同メソッドの逆転バージョン
    ''' </summary>
    ''' <param name="targetByteArray">逆シャッフルを行いたい対象のバイト配列</param>
    ''' <param name="keyNumber">束をいくつ作るかを決めるキー番号</param>
    ''' <returns>元通りに並んだバイト配列</returns>
    ''' <remarks></remarks>
    Public Function DoReversedDealShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

        '###################
        'Phase1.「束」の用意
        '###################

        'まずはkeyNumberを5で割った剰余+2で、束の数を決める
        Dim bunchAmount As Integer = keyNumber Mod 5 + 2

        '上記の数だけ、「束」となるbyte配列のコレクションを作成。
        'いわゆる二次元配列になるので、実装がややトリッキー。注意！
        Dim byteBunch As List(Of List(Of Byte)) = New List(Of List(Of Byte))

        'Maxの6束ぶん、byteBunch内にList(Of Byte)を追加
        For i As Integer = 0 To 5
            byteBunch.Add(New List(Of Byte))
        Next

        '###################################
        'Phase2.入力された配列の「切り分け」
        '###################################

        '続けて、入力された配列を切り分ける。
        'Phase1で計算された束の数で、入力された配列の長さを割る。
        'これにより、束1つあたりの、基本的な配列数が分かる。
        'もし計算に余りが出た場合、その余りを1ずつ、数の若い束に割り振っていく。
        '
        '例:バイト配列長が10で、束の数が6だとする。
        'このとき10÷6=1あまり4で、各束の基本配列数は1。
        'また余りが4なので、この4を数の若い配列4つに追加。
        '結果として、10個の数値を6つの束に分けると、それぞれ長さは2,2,2,2,1,1となる

        Dim basicAmountPerBunch As Integer = targetByteArray.Length \ bunchAmount
        '↑ここは端数切り捨ての商が必要なので"/"で計算してはいけない(VBでは"/"だと商が小数になってしまう)
        Dim remainder As Integer = targetByteArray.Length Mod bunchAmount 'remainder=余り、の意

        '「配り終えた」配列の数
        Dim dealtAmount As Integer = 0

        'For節を用いて、入力されたbyte配列を切り分け
        For i As Integer = 0 To (bunchAmount - 1)

            'この配列に対して数字をいくつ割り振るかを決める
            Dim givingAmount As Integer = basicAmountPerBunch
            'もし現在のiが指す束が、余り-1より小さいなら、余り分の1を追加
            If i < remainder Then
                givingAmount = givingAmount + 1
            End If

            '上記の計算で得られたDealtAmountぶん、入力された配列の数値を渡す
            For j As Integer = 0 To givingAmount - 1
                byteBunch(i).Add(targetByteArray(dealtAmount))
                dealtAmount = dealtAmount + 1
            Next '現在の束への数字割り振り終了

        Next '現在の束への処理終了

        '###################################
        'Phase3.「切り分け」た配列の水平回収
        '###################################

        'こうして切り分けたバイト配列について、1つ目の束の1枚目の数字、
        '2つ目の束の1枚目の数字……n個目の束の1枚目の数字、1つ目の束の2枚目の数字、
        '2つ目の束の2枚目の数字……と、「水平回収」を行い、
        '最後の1つまで数字を回収した後、その結果を返却

        Dim reversedShuffledResult(targetByteArray.Length - 1) As Byte

        '「配り終えた」配列の数(リセットして使いまわし)
        dealtAmount = 0

        '現在数字を「配る」対象となる「束」のアドレス
        Dim bunchAddress As Integer = 0

        '現在カウントしている各束の配列の「深度」。
        '0なら各束の1枚目、1なら各束の2枚目……を指していることになる
        Dim bunchDepth As Integer = 0

        '「水平回収」のアルゴリズムは、DoWhileで実装
        Do While dealtAmount < targetByteArray.Length

            reversedShuffledResult(dealtAmount) = byteBunch(bunchAddress)(bunchDepth)

            dealtAmount = dealtAmount + 1

            'この時点で配列を全て「配り」終えたら、その時点で強制脱出
            If dealtAmount >= targetByteArray.Length Then
                Exit Do
            End If

            '脱出がないなら、bunchAddressを+1
            bunchAddress = bunchAddress + 1

            'この結果、bunchAddressが束の数-1を超過したら、bunchAddressを0にリセットし、
            'bunchDepthに+1(1枚目の束の次の深度の数字を見始める)
            If bunchAddress >= bunchAmount Then
                bunchAddress = 0
                bunchDepth = bunchDepth + 1
            End If
        Loop

        '以上の操作の結果を返却
        Return reversedShuffledResult

    End Function

    ''' <summary>
    ''' 一つ目の引数となったバイト配列に対し、「フラップシャッフル」を行う。
    ''' 「フラップシャッフル」とは、入力されたバイト配列のある1点を分岐点として配列を二分割し、
    ''' それぞれの配列の並び順を逆転させるシャッフル方法を指す造語。
    ''' 分岐点は、入力された(バイト配列の長さ*分岐変数)\10に等しいアドレスとなる。
    ''' 分岐変数は、(keyNumber Mod 7) + 2で決定する。
    ''' たとえば、長さ10の配列に対し、分岐変数=5でフラップシャッフルを行うと、
    ''' (0)~(5)と、(6)~(9)までに二分割される。これらの並び順をそれぞれ入れ替えることで、
    ''' フラップシャッフルは達成される。この場合、結果は次の通りとなる。
    ''' 1,2,3,4,5,6,7,8,9,10→6,5,4,3,2,1,10,9,8,7
    ''' </summary>
    ''' <param name="targetByteArray">シャッフルを行いたい対象のバイト配列</param>
    ''' <param name="keyNumber">配列をどこで二分割するか決めるキー番号</param>
    ''' <returns>フラップシャッフルが行われたバイト配列</returns>
    ''' <remarks></remarks>
    Public Function DoFlapShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

        '###################
        'Phase1.「束」の用意
        '###################

        'まずは、二分割した配列を格納する「束」にあたる変数を用意
        Dim formerBunch As List(Of Byte) = New List(Of Byte)
        Dim latterBunch As List(Of Byte) = New List(Of Byte)

        '###################################################
        'Phase2.分岐変数の計算・配列逆転の起点アドレスを取得
        '###################################################

        '分岐変数はkeyNumber Mod 7 + 2で決定(値は2~8)
        Dim splitVar As Integer = keyNumber Mod 7 + 2

        '逆転起点を、targetByteArray().length*分岐変数\10で決定
        Dim LastAddressOfFormer As Integer = targetByteArray.Length * splitVar \ 10

        '#######################################
        'Phase3.配列の2分割&それぞれの順序を逆転
        '#######################################

        'targetByteArrayの値を、LastAddressOfFormerを臨界点として、2つのListに割り振る
        For i As Integer = 0 To (targetByteArray.Length - 1)

            'iがLastAddressOfFormer以下ならformerBunchへ、そうでないならlatterBunchへ、
            'それぞれ入力値を割り振る
            If i <= LastAddressOfFormer Then
                formerBunch.Add(targetByteArray(i))
            Else
                latterBunch.Add(targetByteArray(i))
            End If

        Next

        'それぞれ、配列を逆転
        formerBunch.Reverse()
        latterBunch.Reverse()

        '####################################
        'Phase4.2つの「束」をまとめ直して返却
        '####################################

        'こうして「配られ」たbyteを、今度は一つの配列にまとめ直す
        Dim shuffledResult As List(Of Byte) = New List(Of Byte)

        For i As Integer = 0 To (formerBunch.Count - 1)
            shuffledResult.Add(formerBunch(i))
        Next

        For i As Integer = 0 To (latterBunch.Count - 1)
            shuffledResult.Add(latterBunch(i))
        Next

        '以上の結果を、配列に変換して返却
        Return shuffledResult.ToArray

    End Function

    ''' <summary>
    ''' DoFlapShuffleToByteArrayメソッドでシャッフルされたバイト配列を、
    ''' 再び元の並びに復元する関数。
    ''' フラップシャッフルはその性質上、同じkeyNumberで同操作をもう一度行えば配列の復元ができるので、
    ''' 実はこの中味はもう一度DoFlapShuffleToByteArrayメソッドを呼び出しているだけ
    ''' </summary>
    ''' <param name="targetByteArray">逆シャッフルを行いたい対象のバイト配列</param>
    ''' <param name="keyNumber">配列をどこで二分割するか決めるキー番号</param>
    ''' <returns>元通りに並んだバイト配列</returns>
    ''' <remarks></remarks>
    Public Function DoReversedFlapShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

        Return DoFlapShuffleToByteArray(targetByteArray, keyNumber)

    End Function

    ''' <summary>
    ''' 一つ目の引数となったバイト配列に対し、「リフルシャッフル」を行う。
    ''' バイト配列をカードの束に見立て、バイト配列を前後で二分割。
    ''' その二分割した束を、前の束を起点として、互い違いに組み合わせていく。
    ''' なお、このメソッドの内部ではリフルシャッフルを(keyNumber Mod 3)+1回繰り返すので、
    ''' リフルシャッフル本体の処理は別のprivateメソッドに切り出す
    ''' </summary>
    ''' <param name="targetByteArray">リフルシャッフルを行いたい対象のバイト配列</param>
    ''' <param name="keyNumber">リフルシャッフルを行う回数を決めるキー番号</param>
    ''' <returns>リフルシャッフルが行われたバイト配列</returns>
    ''' <remarks></remarks>
    Public Function DoRiffleShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

        Dim shuffleTimes As Integer = keyNumber Mod 3 + 1 '(1~3回)

        Dim shuffledResult As Byte() = targetByteArray

        For i As Integer = 1 To shuffleTimes
            shuffledResult = DoRiffleShuffleBody(shuffledResult)
        Next

        Return shuffledResult

    End Function

    ''' <summary>
    ''' リフルシャッフルの処理の実体部分。DoRiffleShuffleToByteArrayメソッドから用いられる
    ''' </summary>
    ''' <param name="targetByteArray">リフルシャッフルを行いたい対象のバイト配列</param>
    ''' <returns>リフルシャッフルが行われたバイト配列</returns>
    ''' <remarks></remarks>
    Private Function DoRiffleShuffleBody(ByVal targetByteArray As Byte()) As Byte()

        '###################
        'Phase1.「束」の用意
        '###################

        'まずは、二分割した配列を格納する「束」にあたる変数を用意
        Dim formerBunch As List(Of Byte) = New List(Of Byte)
        Dim latterBunch As List(Of Byte) = New List(Of Byte)

        '############################################
        'Phase2.入力されたバイト配列を前後で2分割する
        '############################################

        '分割起点を、targetByteArray().length \ 2 - 1で決定
        Dim LastAddressOfFormer As Integer = targetByteArray.Length \ 2 - 1

        For i As Integer = 0 To (targetByteArray.Length - 1)

            '現在のアドレスがLastAddressOfFormer以下なら前の束、そうでないなら後ろの束に、
            '現在のバイトを格納
            If i <= LastAddressOfFormer Then
                formerBunch.Add(targetByteArray(i))
            Else
                latterBunch.Add(targetByteArray(i))
            End If

        Next

        '##################################
        'Phase3.2分割された束をマージ(集束)
        '##################################

        'byteを今度は一つの配列にまとめ直す
        Dim shuffledResult As List(Of Byte) = New List(Of Byte)

        For i As Integer = 0 To (targetByteArray.Length - 1)

            'もしiが偶数ならi\2が指すアドレスである、formerBunchの値を追加。
            'iが奇数なら、latterBunchの値を追加。
            If i Mod 2 = 0 Then
                shuffledResult.Add(formerBunch(i \ 2))
            Else
                shuffledResult.Add(latterBunch(i \ 2))
            End If

        Next

        '以上の結果を、配列に変換して返却
        Return shuffledResult.ToArray

    End Function

    ''' <summary>
    ''' DoRiffleShuffleToByteArrayメソッドでシャッフルされたバイト配列を、
    ''' 再び元の並びに復元する関数。行っている操作は、同メソッドの逆転バージョン。
    ''' やはりリフルシャッフル本体の処理は、別のprivateメソッドに切り出す
    ''' </summary>
    ''' <param name="targetByteArray">逆シャッフルを行いたい対象のバイト配列</param>
    ''' <param name="keyNumber">逆リフルシャッフルを行う回数を決めるキー番号</param>
    ''' <returns>元通りに並んだバイト配列</returns>
    ''' <remarks></remarks>
    Public Function DoReversedRiffleShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

        Dim shuffleTimes As Integer = keyNumber Mod 3 + 1 '(1~3回)

        Dim shuffledResult As Byte() = targetByteArray

        For i As Integer = 1 To shuffleTimes
            shuffledResult = DoReversedRiffleShuffleBody(shuffledResult)
        Next

        '以上の結果を、配列に変換して返却
        Return shuffledResult

    End Function

    ''' <summary>
    ''' 逆リフルシャッフルの処理の実体部分。DoReversedRiffleShuffleToByteArrayメソッドから用いられる
    ''' </summary>
    ''' <param name="targetByteArray">逆リフルシャッフルを行いたい対象のバイト配列</param>
    ''' <returns>逆リフルシャッフルが行われたバイト配列</returns>
    ''' <remarks></remarks>
    Private Function DoReversedRiffleShuffleBody(targetByteArray As Byte()) As Byte()

        '###################
        'Phase1.「束」の用意
        '###################

        'まずは、二分割した配列を格納する「束」にあたる変数を用意
        Dim formerBunch As List(Of Byte) = New List(Of Byte)
        Dim latterBunch As List(Of Byte) = New List(Of Byte)

        '###################################################################
        'Phase2.入力されたバイト配列を、偶数番目と奇数番目ごとに、交互に格納
        '###################################################################

        For i As Integer = 0 To (targetByteArray.Length - 1)

            '現在iの指しているアドレス値が偶数なら、前半の束に格納。
            'そうでないなら、後半の束に格納
            If i Mod 2 = 0 Then
                formerBunch.Add(targetByteArray(i))
            Else
                latterBunch.Add(targetByteArray(i))
            End If

        Next

        '##################################
        'Phase3.2分割された束を重ね合わせる
        '##################################

        'byteを今度は一つの配列にまとめ直す
        Dim shuffledResult As List(Of Byte) = New List(Of Byte)

        For i As Integer = 0 To (formerBunch.Count - 1)
            shuffledResult.Add(formerBunch(i))
        Next
        For i As Integer = 0 To (latterBunch.Count - 1)
            shuffledResult.Add(latterBunch(i))
        Next

        '以上の結果を、配列に変換して返却
        Return shuffledResult.ToArray

    End Function














End Class
