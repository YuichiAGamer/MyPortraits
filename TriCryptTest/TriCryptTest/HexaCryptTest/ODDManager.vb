''' <summary>
''' ORIGIN DeviceCreatorで作成されたデータを保存するバイナリを操作するためのクラス。
''' DeviceCreatorのデータはバイナリ形式で保存し、その拡張子はオリジナルの".odd"である。
''' (odd=ORIGIN Device Dataの意)
''' StreamLineをフル活用することになるので注意！:2013/08/10
''' </summary>
''' <remarks></remarks>
Public Class ODDManager

    'コンストラクタの処理は特になし
    Sub New()

    End Sub

    ''' <summary>
    ''' .odd形式のバイナリを作成するメソッド。
    ''' </summary>
    ''' <param name="binarizedData">バイナリ化されたディヴァイスのデータ(Hexacryptクラスで作成すること)</param>
    ''' <param name="fileName">.oddファイルに与える名前。通常、ディヴァイスの名称と同一とすること</param>
    ''' <param name="directory">.oddファイルを置く場所</param>
    ''' <remarks></remarks>
    Public Sub CreateODDFile(ByVal binarizedData As Byte(), ByVal fileName As String, ByVal directory As String)

        'ファイルのパスの完全版を作成
        Dim path As String = directory & fileName & ".odd"

        'バイナリのライターを作成
        Dim fs As System.IO.FileStream = New System.IO.FileStream(path, IO.FileMode.Create, IO.FileAccess.Write)

        '書き込み開始のポイント。現在ポイントされているのは第何Byte目かを現す
        Dim index As Integer = 0

        'DoWhileループで、書き込みを行っていく
        Do While (index < binarizedData.Length)

            '書き込み単位は1024byte
            Dim writeSize As Integer = 1024

            'もし現在ポイントしているバイトから、1024バイトぶんのバイト配列がない場合、
            '最後の1バイトまでの長さをwriteSizeとする
            If (binarizedData.Length - index) < 1024 Then
                writeSize = binarizedData.Length - index
            End If

            'この状態で、バイナリを書き込んでいく
            fs.Write(binarizedData, index, writeSize)

            '次のループに備え、インデックスをカウントアップ
            index = index + writeSize
        Loop

        '最後に、使ったFileStreamをクローズ。FileStreamは、使用後に必ずクローズすること
        fs.Close()

    End Sub

    ''' <summary>
    ''' .odd形式のバイナリからバイト配列を読み込むメソッド
    ''' </summary>
    ''' <param name="filePath">指定されたファイルのパス</param>
    ''' <returns></returns>
    ''' <remarks></remarks>
    Public Function ReadODDFile(ByVal filePath As String) As Byte()

        Dim extension As String = filePath.Substring(filePath.Length - 4, 4)

        'filePathの拡張子が".odd"でない場合、return Nothing
        If extension.Equals(".odd") = False Then
            Return Nothing
        End If

        'returnされないなら、処理を続行

        'バイナリのリーダーを作成
        Dim fs As System.IO.FileStream = New System.IO.FileStream(filePath, IO.FileMode.Open, IO.FileAccess.Read)

        '読み取り対象のファイルサイズを取得
        Dim fileSize As Integer = CInt(fs.Length)

        '最後に返却するバイト配列を格納するリスト
        Dim scannedBytes(fileSize - 1) As Byte

        '読み込み開始のポイント。現在ポイントされているのは第何Byte目かを現す
        Dim index As Integer = 0

        'DoWhileループで、読み込みを行っていく
        Do While (index < fileSize)

            '書き込み単位は1024byte
            Dim readSize As Integer = 1024

            'もし現在ポイントしているバイトから、1024バイトぶんのバイト配列がない場合、
            '最後の1バイトまでの長さをreadSizeとする
            If (fileSize - index) < 1024 Then
                readSize = fileSize - index
            End If

            'この状態で、バイナリを読み込んでいく
            fs.Read(scannedBytes, index, readSize)

            '次のループに備え、インデックスをカウントアップ
            index = index + readSize
        Loop

        '最後に、使ったFileStreamをクローズ。FileStreamは、使用後に必ずクローズすること
        fs.Close()

        '読み取られたバイト配列を返却
        Return scannedBytes

    End Function

End Class
