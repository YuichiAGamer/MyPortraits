''' <summary>
''' エラトステネスのふるいアルゴリズムによる、素数走査クラス
''' </summary>
''' <remarks></remarks>
Class EratosthenesSieve

    'ローカル変数。これら二つが、それぞれ各整数と、その整数が素数であるか否かのフラグとなる。
    '可読性を上げる目的で、Listのインデックスとそれに対応する数字は同一のものとする。
    '_Numbers(0)=0、Numbers(1)=1、_Numbers(2)=2などなど。
    'そして、_IsPrimeNumberは、そのインデックスの数値が素数であるか否かを表すフラグとなる。
    Private _Numbers As List(Of Integer) = Nothing
    Private _IsPrimeNumber As List(Of Boolean) = Nothing

    'デフォルトでは、素数をいくつまで用意するかを決める定数
    Private Const DEFAULT_MAX_NUMBER As Integer = 65535

    'コンストラクタ。数値をセット後、各整数について素数か否かの判定を行う
    Sub New(Optional ByVal maxNumber As Integer = DEFAULT_MAX_NUMBER)

        Me.SetScanningNumbers(maxNumber)
        Me.DoPrimeNumberCheck()

    End Sub


    ''' <summary>
    ''' 整数を最高いくつまでセットするかを決めるメソッド。
    ''' 別メソッドで、素数判定を行うので、ここでは初期値のセットのみ。
    ''' </summary>
    ''' <param name="maxNumber"></param>
    ''' <remarks></remarks>
    Private Sub SetScanningNumbers(Optional ByVal maxNumber As Integer = DEFAULT_MAX_NUMBER)

        '_Numbersと_IsPrimeNumberを初期化する
        _Numbers = New List(Of Integer)
        _IsPrimeNumber = New List(Of Boolean)

        For i As Integer = 0 To maxNumber
            _Numbers.Add(i)
            _IsPrimeNumber.Add(True)
        Next

    End Sub

    ''' <summary>
    ''' エラトステネスのふるいアルゴリズムで、素数の抜き出しを行う。
    ''' なお、配列のカウント開始番号は0なので、_Numbersのlength-1の数字が、
    ''' 操作すべき最大の整数値であることに注意
    ''' </summary>
    ''' <remarks></remarks>
    Private Sub DoPrimeNumberCheck()

        '最初の判定。まず、0と1は素数ではない(1は数学上、素数と扱われないケースが多い)
        _IsPrimeNumber(0) = False
        _IsPrimeNumber(1) = False

        'エラトステネスのふるいアルゴリズムでは、走査する最大の整数の平方根まで、
        '倍数チェックを行えばいい。よって、この時点で平方根を求め、それを境界条件とする
        Dim scanLimit As Integer = Math.Sqrt(_Numbers.Count - 1) \ 1

        '二重ネストを用いて、現在存在する数値について総当たりを行う。
        '外部のネストは、0~_Numbersの最大の数値までの総当たり、
        '内部のネストは、現在スキャンされている数字*nの総当たりを意味する

        For i As Integer = 0 To (scanLimit + 0)

            'もし現在スキャンされているiが素数でないなら、次のループへ進行
            If _IsPrimeNumber(i) = False Then
                Continue For
            Else
                '素数であるなら、次のループで、以降の倍数をすべて「素数でない」に合わせる
                Dim j As Integer = 2
                Do While (True)
                    _IsPrimeNumber(i * j) = False
                    j = j + 1

                    'もしi * jがこの時点でMaxNumberを超えていたら、強制脱出
                    If (i * j) > (_Numbers.Count - 1) Then
                        Exit Do
                    End If

                Loop

            End If

        Next

    End Sub

    ''' <summary>
    ''' 現在このクラスに格納されている素数を、配列にして返却する
    ''' </summary>
    ''' <returns></returns>
    ''' <remarks></remarks>
    Public Function GetPrimeNumbers()

        Dim result As List(Of Integer) = New List(Of Integer)

        For i As Integer = 0 To (_Numbers.Count - 1)

            If _IsPrimeNumber(i) = True Then
                result.Add(i)
            End If

        Next

        Return result.ToArray

    End Function

    ''' <summary>
    ''' 入力された序列の素数を返す関数。
    ''' 例えば"1"と入力すると、1番目の素数である2が返り、"3"と入力すると3番目の素数である5が返る。
    ''' このクラスが保持している最大の素数を超えた序列が指定された場合、
    ''' もしくは0以下の数字が入力された場合、-1が返る。
    ''' 例えば、このクラスが素数を100個しか保持していない状況で、第101番目の素数を指定した場合は、
    ''' -1が返る。
    ''' </summary>
    ''' <param name="demandedOrder">素数の序列(1以上を入力)</param>
    ''' <returns>指定した序列にある素数</returns>
    ''' <remarks></remarks>
    Public Function GetPrimeNumberByOrder(ByVal demandedOrder As Integer)

        'デフォルト値を設定
        Dim defaultNumber As Integer = -1

        If demandedOrder <= 0 Then
            Return defaultNumber
        End If

        'リターンされなかった場合、For節でこのクラスが保持している整数を総当たり。
        '素数を見つけるたびにカウンタ変数がカウントアップされ、
        'DemandedOrderとカウンタの値が同じになったとき、その素数を返却する
        Dim presentOrder As Integer = 0
        For i As Integer = 1 To (Me._Numbers.Count - 1)

            '現在スキャンしている数字が素数なら、presentOrderを+1
            If _IsPrimeNumber(i) = True Then
                presentOrder = presentOrder + 1
            End If

            'このカウントアップが終わった時点で、presentOrderとdemandedOrderが同じなら、
            '現在のiを、指定された序列の素数として返却
            If presentOrder = demandedOrder Then
                Return i
            End If

        Next

        'ここまで処理が届いたということは、指定された序列の素数が見つからなかったということ。
        '-1を返却する
        Return defaultNumber

    End Function

    ''' <summary>
    ''' 入力された素数が、第何番目の序列かを教える関数。
    ''' 例えば"2"と入力した場合、2は1番目の素数なので1が返る。
    ''' "5"と入力した場合、5は3番目の素数なので3が返る。
    ''' このクラスが保持している最大の素数の序列を上回る数字を入力した場合や、
    ''' 素数以外の数字を入力した場合、-1が返る
    ''' </summary>
    ''' <param name="namedNumber">任意の素数</param>
    ''' <returns>その素数の序列</returns>
    ''' <remarks></remarks>
    Public Function GetOrderOfPrimeNumber(ByVal namedNumber As Integer)

        'デフォルト値を設定
        Dim defaultOrder As Integer = -1

        '指定された数字が、現在保持されている素数の中に存在するかどうかを判定。
        'この結果素数でないと判断されるか、現在保持された最大の数を超過している場合、
        '-1が返る
        If (namedNumber > (Me._Numbers.Count - 1)) Or _
            (Me._IsPrimeNumber(namedNumber) = False) Then
            Return defaultOrder
        End If

        'この時点でリターンがかからないなら、現在保持された素数の中に、
        '入力された素数が存在することになる。その数字が出て来るまで、素数の数を数え上げる
        Dim presentOrder As Integer = 0

        For i As Integer = 0 To (Me._Numbers.Count - 1)

            '現在スキャンしている数が素数なら、presentOrderを+1
            If _IsPrimeNumber(i) = True Then
                presentOrder = presentOrder + 1
            End If

            'このカウントアップ作業を行った時点で、もしiの値がnamedNumberに等しいなら、
            '現在のpresentOrderの値を返却
            If i = namedNumber Then
                Return presentOrder
            End If

        Next

        'ここまで処理が届いたということは、異常が起きたということ。
        'デフォルト値を返却
        Return defaultOrder

    End Function

    ''' <summary>
    ''' 入力された数値を素因数分解し、その結果発見された因数を配列として返却する関数。
    ''' このクラスにより、「素数である」というフラグが立っている数字でのみ素因数分解を試みることで、
    ''' アルゴリズムの高速化を図っている
    ''' </summary>
    ''' <param name="targetNumber">素因数分解</param>
    ''' <returns>素因数分解された数値の配列</returns>
    ''' <remarks></remarks>
    Public Function DecompositeNumber(ByVal targetNumber As Decimal)

        '素数格納用のコレクション。素数が見つかるたびに、ここにその素数を入力していく
        Dim primeNumbers As List(Of Integer) = New List(Of Integer)

        '現在このクラスにカウントされている最大の整数まで、ループを回す
        For i As Integer = 0 To (Me._Numbers.Count - 1)

            '現在のiがもし素数でないなら、個の時点で処理を終えて次のループへ進行。
            'そうでないなら、素数に差し掛かった際の処理を行う
            If _IsPrimeNumber(i) = False Then
                Continue For
            Else
                'ここにかかった時点で現在指しているiは素数。この素数で、targetが何回割り切れるか判定。
                'たとえば入力値が9なら、9は3で2回割れる判定となる
                Do While (targetNumber Mod i = 0)

                    'この節に入ったなら、iでtagetNumberを割り、現在のiの値をリストに追加
                    targetNumber = targetNumber / i
                    primeNumbers.Add(i)

                Loop

            End If '素数判定のif節終了

            '次のループに向かう前に判定。
            'もしこの時点でtargetNumberが1、すなわち素因数分解を終えていたら、
            'これ以上のループは不要なので脱出
            If targetNumber = 1 Then
                Exit For
            End If

        Next '整数走査のFor節終了

        'この時点で得られたリストを、配列に変換して返却
        Dim result() As Integer = primeNumbers.ToArray
        Return result

    End Function

End Class
