''' <summary>
''' バイト配列に対し、可逆的な変化を加えることでその内容を暗号化/復号化するクラス。
''' 現在のところ、バイト配列をカードの束に見立てて、3種類のシャッフル方法で並び順を変化させる
''' 「トライシャッフル(Tri-shuffle)」法と、
''' 外部から入力されたタイムスタンプを利用して、各バイト配列に7回のXor演算を行い暗号化する
''' 「ヘプタプルゾール(Heptaple Xor)法」の、2種類の暗号化方法が存在する。
''' </summary>
''' <remarks></remarks>
Public Class ByteArrayEncrypter

    'コンストラクタでの処理は特になし
    Sub New()

    End Sub

    '######################
    'トライシャッフル法の部
    '######################

    ''' <summary>
    ''' バイト配列をシャッフルし、対クラッカー撹乱を行う「トライシャッフル」を実行。
    ''' トライシャッフルは、ディールシャッフル、フラップシャッフル、リフルシャッフルの3つのシャッフルから成る。
    ''' これらを実行する順序、および各シャッフルのオプションはkeyNumberによって決定される。
    ''' keyNumberは、入力されたバイト配列の合計値で決まる。
    ''' バイト配列の並び順がどのように変わっても、その合計値は変わらないことを利用し、
    ''' そのシャッフルパターンを一意的に同定する。
    ''' </summary>
    ''' <param name="targetArray">トライシャッフルを行いたいバイト配列</param>
    ''' <param name="keyNumber">トライシャッフルを行う際、細かいオプション指定に使うキーナンバー</param>
    ''' <returns>バイト配列にトライシャッフルを行った結果</returns>
    ''' <remarks></remarks>
    Public Function ExecuteTriShuffle(ByVal targetArray As Byte(), ByVal keyNumber As Decimal) As Byte()

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
    Private Function DoDealShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

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
    Private Function DoFlapShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

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
    Private Function DoRiffleShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

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

        '分割起点を、targetByteArray().length ÷ 2の端数切り上げから-1した値で決定
        Dim LastAddressOfFormer As Integer = Math.Ceiling(targetByteArray.Length / 2) - 1

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
    ''' 「トライシャッフル」を逆順で実行するメソッド。
    ''' 逆ディールシャッフル、逆フラップシャッフル、逆リフルシャッフルの3つのシャッフルを、
    ''' keyNumberをもって、逆順で実行する
    ''' </summary>
    ''' <param name="targetArray">逆トライシャッフルを行いたいバイト配列</param>
    ''' <param name="keyNumber">逆トライシャッフルを行う際、細かいオプション指定に使うキーナンバー</param>
    ''' <returns>トライシャッフルから復元されたバイト配列</returns>
    ''' <remarks></remarks>
    Public Function ExecuteReversedTriShuffle(ByVal targetArray() As Byte, ByVal keyNumber As Decimal) As Byte()

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
    ''' DoDealShuffleToByteArrayメソッドでシャッフルされたバイト配列を、
    ''' 再び元の並びに復元する関数。行っている操作は、同メソッドの逆転バージョン
    ''' </summary>
    ''' <param name="targetByteArray">逆シャッフルを行いたい対象のバイト配列</param>
    ''' <param name="keyNumber">束をいくつ作るかを決めるキー番号</param>
    ''' <returns>元通りに並んだバイト配列</returns>
    ''' <remarks></remarks>
    Private Function DoReversedDealShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

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
    ''' DoFlapShuffleToByteArrayメソッドでシャッフルされたバイト配列を、
    ''' 再び元の並びに復元する関数。
    ''' フラップシャッフルはその性質上、同じkeyNumberで同操作をもう一度行えば配列の復元ができるので、
    ''' 実はこの中味はもう一度DoFlapShuffleToByteArrayメソッドを呼び出しているだけ
    ''' </summary>
    ''' <param name="targetByteArray">逆シャッフルを行いたい対象のバイト配列</param>
    ''' <param name="keyNumber">配列をどこで二分割するか決めるキー番号</param>
    ''' <returns>元通りに並んだバイト配列</returns>
    ''' <remarks></remarks>
    Private Function DoReversedFlapShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

        Return DoFlapShuffleToByteArray(targetByteArray, keyNumber)

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
    Private Function DoReversedRiffleShuffleToByteArray(ByVal targetByteArray As Byte(), ByVal keyNumber As Integer)

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

    '######################
    'ヘプタプルゾール法の部
    '######################

    ''' <summary>
    ''' 外部から入力されたタイムスタンプをキーとして、七重のXor演算を行いバイト配列を暗号化する。
    ''' キーとするのは、Xor演算に使う順に列挙して、秒数、分数、時間数、日数、月数、年数の下2桁、
    ''' 年数の上2桁。
    ''' これらの数を8bit=1byteに伸張し、その伸張された1バイトで、暗号化対象となるバイト配列の各要素に、
    ''' Xor演算を行っていく
    ''' </summary>
    ''' <param name="targetArray">暗号化をかけたいバイト配列</param>
    ''' <param name="keyDateTime">暗号化のキーとなるタイムスタンプ</param>
    ''' <returns>暗号化されたバイト配列</returns>
    ''' <remarks></remarks>
    Public Function ExecuteHeptapleXor(ByVal targetArray() As Byte, ByVal keyDateTime As DateTime)

        'まず、入力されたタイムスタンプから、7バイトの配列を生成(秒数→年数上2桁の順序)
        Dim _7BytesForXorOperation() = Me.Generate7BytesFromTimeStamp(keyDateTime, ByteFromTimeStampOrder.SECOND_TO_YEAR)

        'これら7つのバイト数で、Xor演算を7回実行
        For i As Integer = 0 To (_7BytesForXorOperation.Length - 1)
            targetArray = Me.DoXorOperationToByteArray(targetArray, _7BytesForXorOperation(i))
        Next

        '以上の結果を返却
        Return targetArray

    End Function

    ''' <summary>
    ''' 受け取ったタイムスタンプから、7つのバイト数を生成して返却するメソッド。
    ''' 同時にそのバイト配列が昇順か降順かを決めること。
    ''' </summary>
    ''' <param name="keyDateTime">キーとなるタイムスタンプ</param>
    ''' <param name="TimeOrder">バイト配列を時間単位で見た昇順か降順、どちらで並べるかのオプション</param>
    ''' <returns>タイムスタンプから生成された長さ7のバイト配列</returns>
    ''' <remarks></remarks>
    Private Function Generate7BytesFromTimeStamp(ByVal keyDateTime As DateTime,
                                                 ByVal TimeOrder As ByteFromTimeStampOrder) As Byte()

        'まず、入力されたタイムスタンプから、1バイトの値を各々生成する。
        '用いるのはこのクラスのprivateメソッド
        Dim secondAs1Byte As Byte = ExtendTimeUnitNumberTo1Byte(keyDateTime.Second, DemandedBitForTimeUnit.SECOND)
        Dim minuteAs1Byte As Byte = ExtendTimeUnitNumberTo1Byte(keyDateTime.Minute, DemandedBitForTimeUnit.MINUTE)
        Dim hourAs1Byte As Byte = ExtendTimeUnitNumberTo1Byte(keyDateTime.Hour, DemandedBitForTimeUnit.HOUR)
        Dim dayAs1Byte As Byte = ExtendTimeUnitNumberTo1Byte(keyDateTime.Day, DemandedBitForTimeUnit.DAY)
        Dim monthAs1Byte As Byte = ExtendTimeUnitNumberTo1Byte(keyDateTime.Month, DemandedBitForTimeUnit.MONTH)

        '年数については多少処理が複雑。下2桁と上2桁を、それぞれ別々の数字に分解する
        Dim yearBottom2Degree As Integer = CInt(keyDateTime.Year.ToString.Substring(2, 2))
        Dim yearTop2Degree As Integer = CInt(keyDateTime.Year.ToString.Substring(0, 2))

        'そして、これら2つの値からも、バイト数を生成
        Dim yearBottom2DegreeAs1Byte As Byte = ExtendTimeUnitNumberTo1Byte(yearBottom2Degree, DemandedBitForTimeUnit.YEAR_BOTTOM)
        Dim yearTop2DegreeAs1Byte As Byte = ExtendTimeUnitNumberTo1Byte(yearTop2Degree, DemandedBitForTimeUnit.YEAR_BOTTOM)

        '返却するバイト配列を生成
        Dim resultArray(6) As Byte

        '指定されたオプションにより、これを昇順で格納するか、降順で格納するか決める
        If TimeOrder = ByteFromTimeStampOrder.SECOND_TO_YEAR Then
            resultArray(0) = secondAs1Byte
            resultArray(1) = minuteAs1Byte
            resultArray(2) = hourAs1Byte
            resultArray(3) = dayAs1Byte
            resultArray(4) = monthAs1Byte
            resultArray(5) = yearBottom2DegreeAs1Byte
            resultArray(6) = yearTop2DegreeAs1Byte
        Else
            resultArray(0) = yearTop2DegreeAs1Byte
            resultArray(1) = yearBottom2DegreeAs1Byte
            resultArray(2) = monthAs1Byte
            resultArray(3) = dayAs1Byte
            resultArray(4) = hourAs1Byte
            resultArray(5) = minuteAs1Byte
            resultArray(6) = secondAs1Byte
        End If

        'この結果を返却
        Return resultArray

    End Function


    ''' <summary>
    ''' 入力された時間の値、およびその単位から1バイトの値を生成し返却するメソッド。
    ''' その時間単位を表すのに何ビット必要かを元に、8ビット(=1バイト)以上の長さになるよう、
    ''' そのビット配列を繰り返し、これにより出来たビット配列の先頭8ビットで、1バイトの値を生成する。
    ''' </summary>
    ''' <param name="timeUnitNumber">入力された時間の値</param>
    ''' <param name="demandedBit">入力された時間の単位</param>
    ''' <returns>入力された時間とその単位から生成された1バイトの値</returns>
    ''' <remarks></remarks>
    Private Function ExtendTimeUnitNumberTo1Byte(ByVal timeUnitNumber As Integer,
                                                 ByVal demandedBit As DemandedBitForTimeUnit) As Byte

        'まず、入力された値をビット配列に変換
        Dim bitString As String = Convert.ToString(timeUnitNumber, 2)

        'この入力値について、各単位ごとに指定されたビット数を持つよう、左に0を追加
        bitString = Me.ExtendBitString(bitString, demandedBit)

        '以上の操作によって出来たビット配列を、8bit以上になるまで繰り返す、という形で延長
        Do While bitString.Length < 8
            bitString = bitString.Insert(bitString.Length, bitString)
        Loop

        'この結果、bitStringは既に8桁以上存在する。この時点でのbitStringの先頭8桁を回収
        Dim _8Bits As String = bitString.Substring(0, 8)

        '先頭8桁からバイト数を生成し、そして返却
        Dim resultByte As Byte = Convert.ToInt32(_8Bits, 2)

        Return resultByte

    End Function

    ''' <summary>
    ''' 最初の引数に取られたバイト配列の各要素に対し、二番目の引数に指定されたバイト数で
    ''' Xor演算を実行するメソッド。
    ''' </summary>
    ''' <param name="OperatedArray">Xor演算を行う対象のバイト配列</param>
    ''' <param name="OperandByte">バイト配列のXor演算に使う右辺オペランド</param>
    ''' <returns>Xor演算後のバイト配列</returns>
    ''' <remarks></remarks>
    Private Function DoXorOperationToByteArray(ByVal OperatedArray() As Byte, ByVal OperandByte As Byte) As Byte()

        '各配列の要素に対し、Xor演算
        For i As Integer = 0 To (OperatedArray.Length - 1)
            OperatedArray(i) = OperatedArray(i) Xor OperandByte
        Next

        '計算結果を返却
        Return OperatedArray

    End Function

    ''' <summary>
    ''' ヘプタプルゾール法により暗号化されたバイト配列に、逆ヘプタプルゾール法をかけて復号化するメソッド。
    ''' 内容は、Xor演算を逆順に行うところ以外は、正順のヘプタプルゾール法と同じ
    ''' </summary>
    ''' <param name="targetArray">復号化をかけたいバイト配列</param>
    ''' <param name="keyDateTime">復号化のキーとなるタイムスタンプ(暗号化時と同じものを使うこと)</param>
    ''' <returns>復号化されたバイト配列</returns>
    ''' <remarks></remarks>
    Public Function ExecuteReversedHeptapleXor(ByVal targetArray() As Byte, ByVal keyDateTime As DateTime)

        'まず、入力されたタイムスタンプから、7バイトの配列を生成(年数上2桁→秒数の順序)
        Dim _7BytesForXorOperation() = Me.Generate7BytesFromTimeStamp(keyDateTime, ByteFromTimeStampOrder.YEAR_TO_SECOND)

        'これら7つのバイト数で、Xor演算を7回実行
        For i As Integer = 0 To (_7BytesForXorOperation.Length - 1)
            targetArray = Me.DoXorOperationToByteArray(targetArray, _7BytesForXorOperation(i))
        Next

        '以上の結果を返却
        Return targetArray

    End Function


    '######
    '列挙体
    '######

    ''' <summary>
    ''' 各時間単位を表すのに必要なビット数を表した列挙体。
    ''' 例えば秒数の場合、最大値は59。2^5＜59＜2^6より、秒数を表すには6ビットが必要。
    ''' よって秒数の値は6、などと指定されている
    ''' </summary>
    ''' <remarks></remarks>
    Private Enum DemandedBitForTimeUnit As Integer
        SECOND = 6
        MINUTE = 6
        HOUR = 5
        DAY = 5
        MONTH = 4
        YEAR_BOTTOM = 7
        YEAR_TOP = 7
    End Enum

    ''' <summary>
    ''' 時間単位より生成したバイト配列を、どのような順番で並べるかを決める列挙体。
    ''' SECOND_TO_YEARなら、秒数から生成したバイト→分数から生成したバイト→……
    ''' →年数の下2桁から生成したバイト、→年数の上2桁から生成したバイト、の順となる。
    ''' YEAR_TO_SECONDならこの逆
    ''' </summary>
    ''' <remarks></remarks>
    Private Enum ByteFromTimeStampOrder As Integer
        SECOND_TO_YEAR
        YEAR_TO_SECOND
    End Enum

    '######
    'その他
    '######

    ''' <summary>
    ''' ビット配列を表す文字列について、配列が指定された桁数を持つまで、左側に0を追加するメソッド
    ''' </summary>
    ''' <param name="originalBitString">桁数を増やしたいビット配列</param>
    ''' <param name="DemandedDegree">0を追加した後の桁数</param>
    ''' <returns>所定の桁数を持つビット配列</returns>
    ''' <remarks>なお、入力されたビット配列の桁数よりも小さい桁数が指定された場合、
    ''' 入力値をそのまま返却する</remarks>
    Private Function ExtendBitString(ByVal originalBitString As String, ByVal DemandedDegree As Integer) As String

        'もし桁数の指定値が、入力された文字列の桁数以下なら、そのまま入力値を返却
        If DemandedDegree <= originalBitString.Length Then
            Return originalBitString
        End If

        '所定の桁数になるまで、左側に0を追加
        Do While originalBitString.Length < DemandedDegree
            originalBitString = originalBitString.Insert(0, "0")
        Loop

        '桁数が追加されたら、それを返却
        Return originalBitString

    End Function



End Class
