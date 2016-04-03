Public Class Form1

    Dim birthid As New BirthID

    Private hexacrypt As HexaCrypt.HexaCrypt

    Private Sub Form1_Load(sender As System.Object, e As System.EventArgs) Handles MyBase.Load
        hexacrypt = New HexaCrypt.HexaCrypt()

    End Sub

    Private Sub Button1_Click(sender As System.Object, e As System.EventArgs)





        Dim oddM As ODDManager = New ODDManager()

        Dim dir As String = "C:\foo.odd" 'IO.Directory.GetCurrentDirectory()

        'Dim b(255) As Byte
        'Dim rnd As Random = New Random()
        'For i As Integer = 0 To 255

        '    b(i) = i
        'Next

        'oddM.CreateODDFile(b, "foo", dir)

        Dim b() As Byte = oddM.ReadODDFile(dir)

        MessageBox.Show("バイナリー読み込みが完了しました")

        '########
        '########
        '########

        'Dim hN As HexaCrypt.HexalNumber = New HexaCrypt.HexalNumber()

        'hN.SetHexalNumberFromString("1234")

        'Dim former As String = hN.ConvertTo12Bits

        'hN.SetHexalNumberFromString("6543")

        'Dim latter As String = hN.ConvertTo12Bits

        'Dim b As Byte() = hexacrypt.Compress2HexalNumberUnitsInto3Bytes(former, latter)


        'For i As Integer = 0 To b.Length - 1

        '    TextBox3.Text = TextBox3.Text.Insert(TextBox3.Text.Length, b(i).ToString & " ")

        'Next

        'Dim str() As String = hexacrypt.Convert3BytesTo2HexalNumberUnits(b)

        'For i As Integer = 0 To str.Length - 1

        '    TextBox4.Text = TextBox4.Text.Insert(TextBox4.Text.Length, str(i) & " ")

        'Next


        '########
        '########
        '########

        'Dim serialID As String = SerialIDTextBox.Text
        'Dim password As String = PasswordTextBox.Text

        'TextBox3.Text = birthid.GenerateBirthID(serialID, password)

        'Dim bID As String = TextBox3.Text

        'TextBox4.Text = birthid.DecryptSerialIDFromBirthIDAndPassword(bID, password)

        '########
        '########
        '########

        'Dim serialID As String = TextBox1.Text

        'Dim s As String = birthid.MakeInputAs20Degree(CDec(serialID))

        'TextBox2.Text = s

        'Dim b As Byte() = birthid.Split20DegreeDigitTo10Byte(s)

        'TextBox3.Text = ""

        'For i As Integer = 0 To b.Length - 1

        '    TextBox3.Text = TextBox3.Text.Insert(TextBox3.Text.Length, b(i).ToString & " ")

        'Next

        '########
        '########
        '########

        'Dim password As String = TextBox1.Text

        'Dim b As Byte() = birthid.MakePasswordAs10ByteArray(password)

        'TextBox2.Text = ""

        'For i As Integer = 0 To b.Length - 1

        '    TextBox2.Text = TextBox2.Text.Insert(TextBox2.Text.Length, b(i).ToString & " ")

        'Next


    End Sub

    ''' <summary>
    ''' これまで開発した暗号化ソフトの最終結果をテストする
    ''' </summary>
    ''' <param name="sender"></param>
    ''' <param name="e"></param>
    ''' <remarks></remarks>
    Private Sub ExecuteButton_Click(sender As System.Object, e As System.EventArgs) Handles ExecuteButton.Click

        Dim sw As Stopwatch = New Stopwatch()
        sw.Start()

        Dim targetString As String = OriginalStringTextBox.Text

        'D6進数化
        Dim D6DigitString As String = hexacrypt.encryptStringToD6String(targetString)
        D6DigitizedTextBox.Text = D6DigitString

        'バイト配列化
        Dim byteArrayFromD6String() As Byte = hexacrypt.ConvertD6StringToBytes(D6DigitString)

        'バイト配列シャッフル
        Dim bAEncrypter As ByteArrayEncrypter = New ByteArrayEncrypter()
        Dim tStamp As DateTime = Date.Now
        Dim keyNumberFromSecond As Integer = tStamp.Second \ 2

        Dim shuffledByteArray() As Byte = bAEncrypter.ExecuteHeptapleXor(byteArrayFromD6String, tStamp)

        ' HeptapleXor 法による暗号化の結果を表示
        HeptapleXorTextBox.Text = ""

        For i As Integer = 0 To (shuffledByteArray.Length - 1)
            HeptapleXorTextBox.Text =
                HeptapleXorTextBox.Text.Insert(
                    HeptapleXorTextBox.Text.Length, shuffledByteArray(i).ToString & " ")
        Next

        ' 最後に TriShuffle 法を実行
        shuffledByteArray = bAEncrypter.ExecuteTriShuffle(shuffledByteArray, keyNumberFromSecond)

        ' TriShuffle 法による暗号化の結果を表示
        ShuffledByteArrayTextBox.Text = ""

        For i As Integer = 0 To (shuffledByteArray.Length - 1)
            ShuffledByteArrayTextBox.Text =
                ShuffledByteArrayTextBox.Text.Insert(
                    ShuffledByteArrayTextBox.Text.Length, shuffledByteArray(i).ToString & " ")
        Next

        'この状態で、UTF-8として読み込み
        EncryptResultTextBox.Text = System.Text.Encoding.UTF8.GetString(shuffledByteArray)

        'もう一度シャッフル結果を表示
        ByteArrayTextBox2.Text = ""

        For i As Integer = 0 To (shuffledByteArray.Length - 1)
            ByteArrayTextBox2.Text =
                ByteArrayTextBox2.Text.Insert(ByteArrayTextBox2.Text.Length, shuffledByteArray(i).ToString & " ")
        Next

        'TriShuffle 法からの復号化
        Dim decryptResultAsByte() As Byte = bAEncrypter.ExecuteReversedTriShuffle(shuffledByteArray, keyNumberFromSecond)

        ' TriShuffle 法からの復号化の結果を表示
        DeShuffledByteArrayTextBox.Text = ""

        For i As Integer = 0 To (decryptResultAsByte.Length - 1)
            DeShuffledByteArrayTextBox.Text =
                DeShuffledByteArrayTextBox.Text.Insert(
                    DeShuffledByteArrayTextBox.Text.Length, decryptResultAsByte(i).ToString & " ")
        Next

        ' HeptapleXor 法からの復号化
        decryptResultAsByte = bAEncrypter.ExecuteReversedHeptapleXor(decryptResultAsByte, tStamp)

        ' HeptapleXor 法からの復号化の結果を表示
        'DeHeptapleXoredTextBox.Text = ""

        'For i As Integer = 0 To (decryptResultAsByte.Length - 1)
        '    DeHeptapleXoredTextBox.Text =
        '        DeHeptapleXoredTextBox.Text.Insert(
        '            DeHeptapleXoredTextBox.Text.Length, decryptResultAsByte(i).ToString & " ")
        'Next

        'この結果をD6進数に変換
        Dim decryptedD6String As String = hexacrypt.ConvertBytesToD6String(decryptResultAsByte)
        DeHeptapleXoredTextBox.Text = decryptedD6String

        'このD6進数を、もとの文字列に変換
        DecryptedStringTextBox.Text = hexacrypt.decryptStringFromD6String(decryptedD6String)

        sw.Stop()
        Dim t As String = sw.ElapsedMilliseconds.ToString

        MessageBox.Show("この処理にかかった時間は" & t & "ミリ秒です")

        '########
        '########
        '########

        'Dim encryptingString As String = TextBox3.Text
        'Dim D6Units As String = hexacrypt.encryptString(encryptingString)

        'TextBox4.Text = hexacrypt.decryptString(D6Units)

        '########
        '########
        '########

        'Dim str1 As String = "1555"
        'Dim str2 As String = "1666"

        'Dim hNumber As HexaCrypt.HexalNumber = New HexaCrypt.HexalNumber()
        'hNumber.SetHexalNumberFromString(str1)
        'Dim _1 As String = hNumber.HexalNumber
        'str1 = hNumber.ConvertTo12Bits()

        'hNumber.SetHexalNumberFromString(str2)
        'Dim _2 As String = hNumber.HexalNumber
        'str2 = hNumber.ConvertTo12Bits()

        'Dim b() As Byte = hexacrypt.ConvertTwo12BitsInto3Bytes(str1, str2)


        '########
        '########
        '########

        ''入力されたテキストをD6進数に変換(6ユニットでテスト)
        'Dim D6Digits As String = "1111122213331444155516661111"

        ''このD6進数をバイト配列に変換
        'Dim D6DigitsAsBytes() As Byte = hexacrypt.ConvertD6StringToBytes(D6Digits)

        ''こうして出来上がったバイト配列を、.odd形式で保存
        'Dim oddManager As ODDManager = New ODDManager()

        'oddManager.CreateODDFile(D6DigitsAsBytes.ToArray(), "test", "C:\")

        'MessageBox.Show("バイナリー書き込みが完了しました")

        ''続けて、作成されたバイナリから、データの復元を試みる
        'Dim fetchedBytes() As Byte = oddManager.ReadODDFile("C:\test.odd")

        'Dim decryptedD6Digits As String = hexacrypt.ConvertBytesToD6String(fetchedBytes)

        'If D6Digits.Equals(decryptedD6Digits) = False Then
        '    MessageBox.Show("バイナリーのD6進数化に失敗しています")
        'End If

        ''こうして得られた文字列を復号化し、textBox4に展開
        'TextBox4.Text = decryptedD6Digits

        ''MessageBox.Show("バイナリー読み出しが完了しました")

    End Sub
End Class

'Dim d As Date = Date.Now

'Dim dEncrypter As DateEncrypter.DateEncrypter = New DateEncrypter.DateEncrypter()

'Dim dec As Decimal = dEncrypter.GenerateSerialIDFromDate(d)

'TextBox1.Text = dec.ToString

'TextBox2.Text = dEncrypter.DecryptDateFromSerialID(dec.ToString).ToString

'########
'########
'########

'Dim b(47) As Byte

'For i As Integer = 0 To b.Length - 1
'    b(i) = i + 1
'Next

'TextBox1.Text = ""

'For i As Integer = 0 To (b.Length - 1)
'    TextBox1.Text = TextBox1.Text.Insert(TextBox1.Text.Length, b(i).ToString & " ")
'Next

'For i As Integer = 0 To 4
'    b = birthid.DoDealShuffleToByteArray(b, i)
'Next

'TextBox2.Text = ""

'For i As Integer = 0 To (b.Length - 1)
'    TextBox2.Text = TextBox2.Text.Insert(TextBox2.Text.Length, b(i).ToString & " ")
'Next

'For i As Integer = 4 To 0 Step (-1)
'    b = birthid.DoReversedDealShuffleToByteArray(b, i)
'Next

'TextBox3.Text = ""

'For i As Integer = 0 To (b.Length - 1)
'    TextBox3.Text = TextBox3.Text.Insert(TextBox3.Text.Length, b(i).ToString & " ")
'Next

'########
'########
'########

'Dim birthid As New Birthid()

'Dim _20Degree As String = birthid.MakeInputAs20Degree(TextBox1.Text)

'Dim bytes As Byte() = birthid.Split20DegreeDigitTo10Byte(_20Degree)

'TextBox2.Text = ""

'For i As Integer = 0 To bytes.Length - 1
'    TextBox2.Text = TextBox2.Text.Insert(TextBox2.Text.Length, bytes(i).ToString & " ")
'Next

'########
'########
'########

'Dim a As Integer = Convert.ToInt32("1111", 2)
'Dim b As Integer = Convert.ToInt32("0011", 2)

'Dim c As Integer = (a Xor b)

'TextBox1.Text = Convert.ToString(c, 2) & "," & c.ToString

'########
'########
'########

'Dim dEncrypter As DateEncrypter.DateEncrypter = New DateEncrypter.DateEncrypter()

'Dim d As DateTime = Date.Now

'Dim result1 As String = ""
'result1 = dEncrypter.GetMultipledDateData(d).ToString
'TextBox1.Text = result1

'########
'########
'########

'Dim eSieve As EratosthenesSieve = New EratosthenesSieve
'Dim result As Date = dEncrypter.DecryptMultipledDateData(result1)

'TextBox2.Text = result.ToString

'########
'########
'########
'Dim array() As Integer = eratos.GetPrimeNumbers
'Dim text As String = ""

'For i As Integer = 0 To (array.Length - 1)
'    text = text.Insert(text.Length, array(i).ToString & ",")
'Next

'Me.TextBox3.Text = text


'########
'########
'########
'Dim result As String = hexacrypt.encryptString(Me.TextBox1.Text)

'Dim result2 As String = hexacrypt.decryptString(result)

''この結果の中に含まれる1~6の数をそれぞれカウント
'Dim amount() As Integer = {0, 0, 0, 0, 0, 0}

'For i As Integer = 0 To result.Length - 1
'    Dim str As String = result.Substring(i, 1)

'    If str = "1" Then
'        amount(0) = amount(0) + 1
'    ElseIf str = "2" Then
'        amount(1) = amount(1) + 1
'    ElseIf str = "3" Then
'        amount(2) = amount(2) + 1
'    ElseIf str = "4" Then
'        amount(3) = amount(3) + 1
'    ElseIf str = "5" Then
'        amount(4) = amount(4) + 1
'    ElseIf str = "6" Then
'        amount(5) = amount(5) + 1
'    End If

'Next

'Me.TextBox2.Text = result
'Me.TextBox3.Text = result2

'MessageBox.Show("1の出た回数:" & amount(0).ToString & vbNewLine & _
'                "2の出た回数:" & amount(1).ToString & vbNewLine & _
'                "3の出た回数:" & amount(2).ToString & vbNewLine & _
'                "4の出た回数:" & amount(3).ToString & vbNewLine & _
'                "5の出た回数:" & amount(4).ToString & vbNewLine & _
'                "6の出た回数:" & amount(5).ToString & vbNewLine)
'########
'########
'########