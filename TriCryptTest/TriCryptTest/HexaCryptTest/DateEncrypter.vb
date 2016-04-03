Namespace DateEncrypter

    ''' <summary>
    ''' YYYY/MM/DD/hh:mm:ss形式の時刻を、素数を利用した暗号に変更するクラス。
    ''' エラトステネスのふるいアルゴリズムを用いて素数を動的に捜索して、
    ''' その素数をキーに暗号を作成する。
    ''' こうして生成された暗号を、ディヴァイスカードのシリアルIDとして利用する
    ''' </summary>
    ''' <remarks></remarks>
    Public Class DateEncrypter

        '##########
        'メンバ変数
        '##########

        'エラトステネスのふるいで求めた素数を格納しておくメンバ変数
        Private eSieve As EratosthenesSieve = Nothing

        '以下、素数の掛け算を行った結果は32bitでは表しきれなくなる場合もあり、
        'かつ厳密な数値計算も要求されるべきなので、型はDecimalで宣言

        '各秒数に対応する素数のマッピング結果を記録する素数。
        '00秒~59秒まで、60個の素数が格納されている。
        Private PNumbersForSecond(59) As Decimal 'PNumbers=Prime Numbersの意

        '各分数に対応する素数のマッピング結果を記録する素数。
        '00分~59分まで、60個の素数が格納されている。
        Private PNumbersForMinute(59) As Decimal

        '各時間数に対応する素数のマッピング結果を記録する素数。
        '00時~23時まで、24個の素数が格納されている。
        Private PNumbersForHour(23) As Decimal

        '各日数に対応する素数のマッピング結果を記録する素数。
        '1日~31日まで、31個の素数が格納されている。
        Private PNumbersForDay(30) As Decimal

        '各月数に対応する素数のマッピング結果を記録する素数。
        '1月~12月まで、12個の素数が格納されている。
        Private PNumbersForMonth(11) As Decimal

        '各年数に対応する素数のマッピング結果を記録する素数。
        'なお、基準年(1年目)はUnixエポックに指定されている1970年とし、
        'エポックから起算して現在は何年目かで、年数と素数の紐付けを行う。
        '例えば、西暦2013年は2013-1970=43で、43+1=エポックから44年目となる。
        'よってこの配列の要素数43番に格納された素数が、対応する素数となる。
        '仕様上、このクラスが使えるのは西暦2069年までとなる
        Private PNumbersForYear(99) As Decimal

        '################
        'メンバ変数(定数)
        '################

        'Unixエポックの西暦数を表す定数
        Private Const UNIX_EPOCH_YEAR As Integer = 1970

        'コンストラクタ
        Sub New()

            'まずは、EratosthenesSieveクラスで、素数を用意
            eSieve = New EratosthenesSieve()

            '続けて、eSieveを利用して、年月日時分秒と素数のマッピングを動的に作成
            MakeDateDataMappingForPrimeNumbers()

        End Sub

        ''' <summary>
        ''' 各素数と、年月日時分秒のそれぞれ値をマッピングする
        ''' </summary>
        ''' <remarks></remarks>
        Private Sub MakeDateDataMappingForPrimeNumbers()

            '現在何番目の素数を用いているかをカウントする変数。
            'EratosthenesSieveのGetPrimeNumberByOrder()メソッドの仕様上、1からスタート
            Dim PNumberOrder As Integer = 1

            '以下、紐付けを開始。順序は秒、分、時、日、月、年の順とする。
            '秒数などのように振れ幅の大きい数値に対して小さい素数を割り振った方が、
            '出力結果のばらつきも大きくなりクラッカーへ対する撹乱効果も期待できるため

            '秒数のマッピング(偶数の部)。秒数マッピングについては、偶数と奇数の部で二段構えで行い、
            'それぞれ昇順/降順でマッピングを行うことで、クラッカーへの更なる撹乱効果の上昇を見込む
            For i As Integer = 0 To 58 Step 2
                PNumbersForSecond(i) = eSieve.GetPrimeNumberByOrder(PNumberOrder)
                'PNumberOrderをインクリメント
                PNumberOrder = PNumberOrder + 1
            Next

            '秒数のマッピング(奇数の部)
            For i As Integer = 59 To 1 Step (-2)
                PNumbersForSecond(i) = eSieve.GetPrimeNumberByOrder(PNumberOrder)
                'PNumberOrderをインクリメント
                PNumberOrder = PNumberOrder + 1
            Next

            '分数のマッピング
            For i As Integer = 0 To 59
                PNumbersForMinute(i) = eSieve.GetPrimeNumberByOrder(PNumberOrder)
                'PNumberOrderをインクリメント
                PNumberOrder = PNumberOrder + 1
            Next

            '時間数のマッピング
            For i As Integer = 0 To 23
                PNumbersForHour(i) = eSieve.GetPrimeNumberByOrder(PNumberOrder)
                'PNumberOrderをインクリメント
                PNumberOrder = PNumberOrder + 1
            Next

            '日数のマッピング
            For i As Integer = 0 To 30
                PNumbersForDay(i) = eSieve.GetPrimeNumberByOrder(PNumberOrder)
                'PNumberOrderをインクリメント
                PNumberOrder = PNumberOrder + 1
            Next

            '月数のマッピング
            For i As Integer = 0 To 11
                PNumbersForMonth(i) = eSieve.GetPrimeNumberByOrder(PNumberOrder)
                'PNumberOrderをインクリメント
                PNumberOrder = PNumberOrder + 1
            Next

            '年数のマッピング
            For i As Integer = 0 To 99
                PNumbersForYear(i) = eSieve.GetPrimeNumberByOrder(PNumberOrder)
                'PNumberOrderをインクリメント
                PNumberOrder = PNumberOrder + 1
            Next

        End Sub

        ''' <summary>
        ''' 入力された年月日時分秒から、シリアルIDを生成するメソッド。
        ''' シリアルIDは各年月日時分秒の数値に対応するようマッピングされた素数の積を文字列化し、
        ''' 更に文字列の配列を逆転させ、最後にリフルシャッフルを行うことで生成する
        ''' </summary>
        ''' <param name="seedDate">シリアルIDのシードとなる年月日時分秒</param>
        ''' <returns>生成されたシリアルID</returns>
        ''' <remarks></remarks>
        Public Function GenerateSerialIDFromDate(ByVal seedDate As Date) As Decimal

            Dim generatedID As String = Me.EncryptToMultipledDateData(seedDate).ToString

            generatedID = StrReverse(generatedID)

            generatedID = DoRiffleShuffleToString(generatedID)

            Return generatedID

        End Function

        ''' <summary>
        ''' 入力されたシリアルIDから、年月日時分秒を復元するメソッド。
        ''' シリアルIDはまず文字列に読み替えられ、逆リフルシャッフル→文字列逆転で素数積の形に復元され、
        ''' 最後に素数積の素因数分解から6つの素数を得て、対応するマッピングの年月日時分秒を取得する
        ''' </summary>
        ''' <param name="serialID">年月日時分秒を復元させるシリアルID</param>
        ''' <returns>復元された年月日時分秒</returns>
        ''' <remarks></remarks>
        Public Function DecryptDateFromSerialID(ByVal serialID As Decimal) As Date

            Dim strID As String = serialID.ToString

            strID = DoReversedRiffleShuffleToString(strID)

            strID = StrReverse(strID)

            Dim DecryptedDateData As Decimal = CDec(strID)

            Return DecryptFromMultipledDateData(DecryptedDateData)

        End Function

        ''' <summary>
        ''' シリアルIDにリフルシャッフルを行う関数。
        ''' BirthidクラスのDoRiffleShuffleToByteArrayメソッドを流用。
        ''' 複数回リフルシャッフルを行いたいなら、繰り返し用いること
        ''' </summary>
        ''' <param name="targetString">リフルシャッフルを行いたい対象の文字列</param>
        ''' <returns>リフルシャッフルが行われた文字列</returns>
        ''' <remarks></remarks>
        Private Function DoRiffleShuffleToString(ByVal targetString As String) As String

            '###################
            'Phase1.「束」の用意
            '###################

            'まずは、二分割した配列を格納する「束」にあたる変数を用意
            Dim formerBunch As List(Of String) = New List(Of String)
            Dim latterBunch As List(Of String) = New List(Of String)

            '########################################
            'Phase2.入力された文字列を前後で2分割する
            '########################################

            '分割起点を、targetString().length \ 2で決定
            Dim LastAddressOfFormer As Integer = targetString.Length \ 2

            For i As Integer = 0 To (targetString.Length - 1)

                '現在のアドレスがLastAddressOfFormer以下なら前の束、そうでないなら後ろの束に、
                '現在のアドレスが指している文字1文字を格納
                If i <= LastAddressOfFormer Then
                    formerBunch.Add(targetString.Substring(i, 1))
                Else
                    latterBunch.Add(targetString.Substring(i, 1))
                End If

            Next

            '##################################
            'Phase3.2分割された束をマージ(集束)
            '##################################

            '文字列を、今度は一つの配列にまとめ直す
            Dim shuffledResult As List(Of String) = New List(Of String)

            For i As Integer = 0 To (targetString.Length - 1)

                'もしiが偶数ならi\2が指すアドレスである、formerBunchの値を追加。
                'iが奇数なら、latterBunchの値を追加。
                If i Mod 2 = 0 Then
                    shuffledResult.Add(formerBunch(i \ 2))
                Else
                    shuffledResult.Add(latterBunch(i \ 2))
                End If

            Next

            '以上の結果を、再度一つの文字列に連結して返却

            Dim finalString As String = ""

            For i As Integer = 0 To shuffledResult.Count - 1
                finalString = finalString.Insert(finalString.Length, shuffledResult(i))
            Next

            Return finalString

        End Function

        ''' <summary>
        ''' 逆リフルシャッフルの処理の実体部分。
        ''' やはりBirthidクラスのDoReversedRiffleShuffleToByteArrayメソッドを流用。
        ''' </summary>
        ''' <param name="targetString">逆リフルシャッフルを行いたい対象の文字列</param>
        ''' <returns>逆リフルシャッフルが行われた文字列</returns>
        ''' <remarks></remarks>
        Private Function DoReversedRiffleShuffleToString(targetString As String) As String

            '###################
            'Phase1.「束」の用意
            '###################

            'まずは、二分割した配列を格納する「束」にあたる変数を用意
            Dim formerBunch As List(Of String) = New List(Of String)
            Dim latterBunch As List(Of String) = New List(Of String)

            '########################################################################
            'Phase2.入力された文字列を、偶数番目と奇数番目ごとに、1文字ずつ交互に格納
            '########################################################################

            For i As Integer = 0 To (targetString.Length - 1)

                '現在iの指しているアドレス値が偶数なら、前半の束に格納。
                'そうでないなら、後半の束に格納
                If i Mod 2 = 0 Then
                    formerBunch.Add(targetString.Substring(i, 1))
                Else
                    latterBunch.Add(targetString.Substring(i, 1))
                End If

            Next

            '##################################
            'Phase3.2分割された束を重ね合わせる
            '##################################

            'byteを今度は一つの配列にまとめ直す
            Dim shuffledResult As List(Of String) = New List(Of String)

            For i As Integer = 0 To (formerBunch.Count - 1)
                shuffledResult.Add(formerBunch(i))
            Next
            For i As Integer = 0 To (latterBunch.Count - 1)
                shuffledResult.Add(latterBunch(i))
            Next

            '以上の結果を、再度一つの文字列に連結して返却

            Dim finalString As String = ""

            For i As Integer = 0 To shuffledResult.Count - 1
                finalString = finalString.Insert(finalString.Length, shuffledResult(i))
            Next

            Return finalString

        End Function

        ''' <summary>
        ''' 入力された年月日時分秒のデータをもとに、
        ''' それぞれの値に対応した素数を呼び出し、乗算する関数
        ''' </summary>
        ''' <param name="inputDate">暗号化したい年月日時分秒のデータ</param>
        ''' <returns>なお、アルゴリズム簡略化の目的で、世界協定時刻は不使用</returns>
        ''' <remarks></remarks>
        Private Function EncryptToMultipledDateData(ByVal inputDate As Date)

            '返却結果の初期値を設定。行うのは掛け算なので、初期値は当然1である
            Dim result As Decimal = 1

            '入力された秒数を取得し、対応する素数で乗算
            result = result * PNumbersForSecond(inputDate.Second)

            '入力された分数を取得し、対応する素数で乗算
            result = result * PNumbersForMinute(inputDate.Minute)

            '入力された時間数を取得し、対応する素数で乗算
            result = result * PNumbersForHour(inputDate.Hour)

            '入力された日数を取得し、対応する素数で乗算。
            'なお日数は0で始まりではなく1で始まりなので、配列の要素番号を-1で補正
            result = result * PNumbersForDay(inputDate.Day - 1)

            '入力された月数を取得し、対応する素数で乗算
            'なお月数は0で始まりではなく1で始まりなので、配列の要素番号を-1で補正
            result = result * PNumbersForMonth(inputDate.Month - 1)

            '入力された年数を取得し、対応する素数で乗算。
            'なお年数はUnixエポックから開始されるので、受け取った西暦数をUnixエポック分減算
            result = result * PNumbersForYear(inputDate.Year - UNIX_EPOCH_YEAR)

            'この計算結果を返却
            Return result

        End Function

        ''' <summary>
        ''' 年月日時分秒から生成された素数の積を、再び年月日時分秒に戻す関数
        ''' </summary>
        ''' <param name="MultipledDateData">年月日時分秒から生成された素数の積</param>
        ''' <returns>復号された年月日時分秒</returns>
        ''' <remarks></remarks>
        Public Function DecryptFromMultipledDateData(ByVal MultipledDateData As Decimal)

            'まずは入力された素数を素因数分解し、その結果を取得 
            Dim primeNumbers() As Integer = eSieve.DecompositeNumber(MultipledDateData)

            '続けて、素因数分解の結果を、DetectMappedDateElementに格納
            Dim DateData As List(Of String()) = New List(Of String())

            For i As Integer = 0 To (primeNumbers.Length - 1)
                DateData.Add(Me.DetectMappedDateElement(primeNumbers(i)))
            Next

            'DateDataの中にある、年月日時分秒のデータを、それぞれ回収。
            'DataData内のデータは、DetectMappedDateElementメソッドで各種補正がなされているので、
            'そのまま使用可能
            Dim year As Integer = 1
            Dim month As Integer = 1
            Dim day As Integer = 1
            Dim hour As Integer = 0
            Dim minute As Integer = 0
            Dim second As Integer = 0


            For i As Integer = 0 To (DateData.Count - 1)

                If (DateData(i)(0) = "Year") Then
                    '中味がYearである場合、年数をセット
                    year = CInt(DateData(i)(1))
                ElseIf (DateData(i)(0) = "Month") Then
                    '中味がMonthである場合、月数をセット
                    month = CInt(DateData(i)(1))
                ElseIf (DateData(i)(0) = "Day") Then
                    '中味がDayである場合、日数をセット
                    day = CInt(DateData(i)(1))
                ElseIf (DateData(i)(0) = "Hour") Then
                    '中味がHourである場合、時間数をセット
                    hour = CInt(DateData(i)(1))
                ElseIf (DateData(i)(0) = "Minute") Then
                    '中味がMinuteである場合、分数をセット
                    minute = CInt(DateData(i)(1))
                ElseIf (DateData(i)(0) = "Second") Then
                    '中味がSecondである場合、秒数をセット
                    second = CInt(DateData(i)(1))
                End If

            Next

            '返却用の年月日時分秒
            Dim decryptedDate As DateTime = New DateTime(year, month, day, hour, minute, second)

            Return decryptedDate
        End Function


        ''' <summary>
        ''' 入力された素数が、このクラスにマッピングされたどの年月日時分秒と紐付いているかを
        ''' 解析し、返却する関数。
        ''' 返却形式はString(1)で、一つ目の要素に"Second","Minute","Hour","Day","Month","Year"の
        ''' いずれかが入り、二つ目の要素にその数値が入る。
        ''' 入力値が素数でないか、素数であってもこのクラスにマッピングされていない場合、
        ''' "NoData","-1"が格納された配列が返る
        ''' </summary>
        ''' <param name="primeNumber">紐付け先を知りたい素数</param>
        ''' <returns>紐付けされた年月日時分秒のデータ</returns>
        ''' <remarks></remarks>
        Private Function DetectMappedDateElement(ByVal primeNumber As Integer)

            '返却値の初期値を設定
            Dim result As String() = {"NoData", "-1"}

            '入力された素数が秒数を指しているかどうかチェック
            For i As Integer = 0 To PNumbersForSecond.Length - 1
                If primeNumber = PNumbersForSecond(i) Then
                    result(0) = "Second"
                    result(1) = i.ToString
                    Return result
                End If
            Next

            '入力された素数が分数を指しているかどうかチェック
            For i As Integer = 0 To PNumbersForMinute.Length - 1
                If primeNumber = PNumbersForMinute(i) Then
                    result(0) = "Minute"
                    result(1) = i.ToString
                    Return result
                End If
            Next

            '入力された素数が時間数を指しているかどうかチェック
            For i As Integer = 0 To PNumbersForHour.Length - 1
                If primeNumber = PNumbersForHour(i) Then
                    result(0) = "Hour"
                    result(1) = i.ToString
                    Return result
                End If
            Next

            '入力された素数が日数を指しているかどうかチェック。
            '日数は1で始まりなので、返却値はi+1
            For i As Integer = 0 To PNumbersForDay.Length - 1
                If primeNumber = PNumbersForDay(i) Then
                    result(0) = "Day"
                    result(1) = (i + 1).ToString
                    Return result
                End If
            Next

            '入力された素数が月数を指しているかどうかチェック。
            '月数は1で始まりなので、返却値はi+1
            For i As Integer = 0 To PNumbersForMonth.Length - 1
                If primeNumber = PNumbersForMonth(i) Then
                    result(0) = "Month"
                    result(1) = (i + 1).ToString
                    Return result
                End If
            Next

            '入力された素数が年数を指しているかどうかチェック。
            '年数はUnixエポックで始まりなので、返却値はi+UNIX_EPOCH_YEAR
            For i As Integer = 0 To PNumbersForYear.Length - 1
                If primeNumber = PNumbersForYear(i) Then
                    result(0) = "Year"
                    result(1) = (i + UNIX_EPOCH_YEAR).ToString
                    Return result
                End If
            Next

            '上記のどれにも引っかからないなら、デフォルト値を返却
            Return result
        End Function

    End Class

End Namespace
