<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class Form1
    Inherits System.Windows.Forms.Form

    'フォームがコンポーネントの一覧をクリーンアップするために dispose をオーバーライドします。
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Windows フォーム デザイナーで必要です。
    Private components As System.ComponentModel.IContainer

    'メモ: 以下のプロシージャは Windows フォーム デザイナーで必要です。
    'Windows フォーム デザイナーを使用して変更できます。  
    'コード エディターを使って変更しないでください。
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(Form1))
        Me.Label3 = New System.Windows.Forms.Label()
        Me.OriginalStringTextBox = New System.Windows.Forms.TextBox()
        Me.ExecuteButton = New System.Windows.Forms.Button()
        Me.Label4 = New System.Windows.Forms.Label()
        Me.D6DigitizedTextBox = New System.Windows.Forms.TextBox()
        Me.Label1 = New System.Windows.Forms.Label()
        Me.Label2 = New System.Windows.Forms.Label()
        Me.Label5 = New System.Windows.Forms.Label()
        Me.Label6 = New System.Windows.Forms.Label()
        Me.Label7 = New System.Windows.Forms.Label()
        Me.ShuffledByteArrayTextBox = New System.Windows.Forms.TextBox()
        Me.Label8 = New System.Windows.Forms.Label()
        Me.HeptapleXorTextBox = New System.Windows.Forms.TextBox()
        Me.EncryptResultTextBox = New System.Windows.Forms.TextBox()
        Me.Label9 = New System.Windows.Forms.Label()
        Me.Label10 = New System.Windows.Forms.Label()
        Me.Label11 = New System.Windows.Forms.Label()
        Me.ByteArrayTextBox2 = New System.Windows.Forms.TextBox()
        Me.Label12 = New System.Windows.Forms.Label()
        Me.DeShuffledByteArrayTextBox = New System.Windows.Forms.TextBox()
        Me.Label13 = New System.Windows.Forms.Label()
        Me.Label14 = New System.Windows.Forms.Label()
        Me.Label15 = New System.Windows.Forms.Label()
        Me.DeHeptapleXoredTextBox = New System.Windows.Forms.TextBox()
        Me.Label16 = New System.Windows.Forms.Label()
        Me.DecryptedStringTextBox = New System.Windows.Forms.TextBox()
        Me.Label17 = New System.Windows.Forms.Label()
        Me.SuspendLayout()
        '
        'Label3
        '
        Me.Label3.AutoSize = True
        Me.Label3.Location = New System.Drawing.Point(46, 13)
        Me.Label3.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label3.Name = "Label3"
        Me.Label3.Size = New System.Drawing.Size(98, 18)
        Me.Label3.TabIndex = 6
        Me.Label3.Text = "原形文字列"
        '
        'OriginalStringTextBox
        '
        Me.OriginalStringTextBox.Location = New System.Drawing.Point(50, 35)
        Me.OriginalStringTextBox.Margin = New System.Windows.Forms.Padding(4)
        Me.OriginalStringTextBox.Multiline = True
        Me.OriginalStringTextBox.Name = "OriginalStringTextBox"
        Me.OriginalStringTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Vertical
        Me.OriginalStringTextBox.Size = New System.Drawing.Size(374, 59)
        Me.OriginalStringTextBox.TabIndex = 5
        '
        'ExecuteButton
        '
        Me.ExecuteButton.Location = New System.Drawing.Point(402, 619)
        Me.ExecuteButton.Margin = New System.Windows.Forms.Padding(4)
        Me.ExecuteButton.Name = "ExecuteButton"
        Me.ExecuteButton.Size = New System.Drawing.Size(156, 36)
        Me.ExecuteButton.TabIndex = 7
        Me.ExecuteButton.Text = "Execute"
        Me.ExecuteButton.UseVisualStyleBackColor = True
        '
        'Label4
        '
        Me.Label4.AutoSize = True
        Me.Label4.Location = New System.Drawing.Point(46, 137)
        Me.Label4.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label4.Name = "Label4"
        Me.Label4.Size = New System.Drawing.Size(258, 18)
        Me.Label4.TabIndex = 9
        Me.Label4.Text = "HexaCrypt 法による暗号化の結果"
        '
        'D6DigitizedTextBox
        '
        Me.D6DigitizedTextBox.BackColor = System.Drawing.SystemColors.ControlDark
        Me.D6DigitizedTextBox.Location = New System.Drawing.Point(50, 158)
        Me.D6DigitizedTextBox.Margin = New System.Windows.Forms.Padding(4)
        Me.D6DigitizedTextBox.Multiline = True
        Me.D6DigitizedTextBox.Name = "D6DigitizedTextBox"
        Me.D6DigitizedTextBox.ReadOnly = True
        Me.D6DigitizedTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Vertical
        Me.D6DigitizedTextBox.Size = New System.Drawing.Size(374, 59)
        Me.D6DigitizedTextBox.TabIndex = 8
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Location = New System.Drawing.Point(334, 109)
        Me.Label1.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(26, 18)
        Me.Label1.TabIndex = 10
        Me.Label1.Text = "↓"
        '
        'Label2
        '
        Me.Label2.AutoSize = True
        Me.Label2.Location = New System.Drawing.Point(334, 241)
        Me.Label2.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(26, 18)
        Me.Label2.TabIndex = 11
        Me.Label2.Text = "↓"
        '
        'Label5
        '
        Me.Label5.AutoSize = True
        Me.Label5.Location = New System.Drawing.Point(334, 497)
        Me.Label5.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label5.Name = "Label5"
        Me.Label5.Size = New System.Drawing.Size(26, 18)
        Me.Label5.TabIndex = 17
        Me.Label5.Text = "↓"
        '
        'Label6
        '
        Me.Label6.AutoSize = True
        Me.Label6.Location = New System.Drawing.Point(334, 365)
        Me.Label6.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label6.Name = "Label6"
        Me.Label6.Size = New System.Drawing.Size(26, 18)
        Me.Label6.TabIndex = 16
        Me.Label6.Text = "↓"
        '
        'Label7
        '
        Me.Label7.AutoSize = True
        Me.Label7.Location = New System.Drawing.Point(46, 392)
        Me.Label7.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label7.Name = "Label7"
        Me.Label7.Size = New System.Drawing.Size(251, 18)
        Me.Label7.TabIndex = 15
        Me.Label7.Text = "TriShuffle 法による暗号化の結果"
        '
        'ShuffledByteArrayTextBox
        '
        Me.ShuffledByteArrayTextBox.BackColor = System.Drawing.SystemColors.ControlDark
        Me.ShuffledByteArrayTextBox.Location = New System.Drawing.Point(50, 414)
        Me.ShuffledByteArrayTextBox.Margin = New System.Windows.Forms.Padding(4)
        Me.ShuffledByteArrayTextBox.Multiline = True
        Me.ShuffledByteArrayTextBox.Name = "ShuffledByteArrayTextBox"
        Me.ShuffledByteArrayTextBox.ReadOnly = True
        Me.ShuffledByteArrayTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Vertical
        Me.ShuffledByteArrayTextBox.Size = New System.Drawing.Size(374, 59)
        Me.ShuffledByteArrayTextBox.TabIndex = 14
        '
        'Label8
        '
        Me.Label8.AutoSize = True
        Me.Label8.Location = New System.Drawing.Point(46, 269)
        Me.Label8.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label8.Name = "Label8"
        Me.Label8.Size = New System.Drawing.Size(271, 18)
        Me.Label8.TabIndex = 13
        Me.Label8.Text = "HeptapleXor 法による暗号化の結果"
        '
        'HeptapleXorTextBox
        '
        Me.HeptapleXorTextBox.BackColor = System.Drawing.SystemColors.ControlDark
        Me.HeptapleXorTextBox.Location = New System.Drawing.Point(50, 290)
        Me.HeptapleXorTextBox.Margin = New System.Windows.Forms.Padding(4)
        Me.HeptapleXorTextBox.Multiline = True
        Me.HeptapleXorTextBox.Name = "HeptapleXorTextBox"
        Me.HeptapleXorTextBox.ReadOnly = True
        Me.HeptapleXorTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Vertical
        Me.HeptapleXorTextBox.Size = New System.Drawing.Size(374, 59)
        Me.HeptapleXorTextBox.TabIndex = 12
        '
        'EncryptResultTextBox
        '
        Me.EncryptResultTextBox.BackColor = System.Drawing.SystemColors.ControlDark
        Me.EncryptResultTextBox.Location = New System.Drawing.Point(309, 552)
        Me.EncryptResultTextBox.Margin = New System.Windows.Forms.Padding(4)
        Me.EncryptResultTextBox.Multiline = True
        Me.EncryptResultTextBox.Name = "EncryptResultTextBox"
        Me.EncryptResultTextBox.ReadOnly = True
        Me.EncryptResultTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Vertical
        Me.EncryptResultTextBox.Size = New System.Drawing.Size(374, 59)
        Me.EncryptResultTextBox.TabIndex = 18
        '
        'Label9
        '
        Me.Label9.AutoSize = True
        Me.Label9.Location = New System.Drawing.Point(305, 530)
        Me.Label9.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label9.Name = "Label9"
        Me.Label9.Size = New System.Drawing.Size(192, 18)
        Me.Label9.TabIndex = 19
        Me.Label9.Text = "文字列化の結果(UTF-8)"
        '
        'Label10
        '
        Me.Label10.AutoSize = True
        Me.Label10.Location = New System.Drawing.Point(595, 365)
        Me.Label10.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label10.Name = "Label10"
        Me.Label10.Size = New System.Drawing.Size(26, 18)
        Me.Label10.TabIndex = 30
        Me.Label10.Text = "↑"
        '
        'Label11
        '
        Me.Label11.AutoSize = True
        Me.Label11.Location = New System.Drawing.Point(541, 392)
        Me.Label11.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label11.Name = "Label11"
        Me.Label11.Size = New System.Drawing.Size(320, 18)
        Me.Label11.TabIndex = 29
        Me.Label11.Text = "TriCrypt 法によって得た最終結果の再表示"
        '
        'ByteArrayTextBox2
        '
        Me.ByteArrayTextBox2.BackColor = System.Drawing.SystemColors.ControlDark
        Me.ByteArrayTextBox2.Location = New System.Drawing.Point(544, 414)
        Me.ByteArrayTextBox2.Margin = New System.Windows.Forms.Padding(4)
        Me.ByteArrayTextBox2.Multiline = True
        Me.ByteArrayTextBox2.Name = "ByteArrayTextBox2"
        Me.ByteArrayTextBox2.ReadOnly = True
        Me.ByteArrayTextBox2.ScrollBars = System.Windows.Forms.ScrollBars.Vertical
        Me.ByteArrayTextBox2.Size = New System.Drawing.Size(374, 59)
        Me.ByteArrayTextBox2.TabIndex = 28
        '
        'Label12
        '
        Me.Label12.AutoSize = True
        Me.Label12.Location = New System.Drawing.Point(541, 268)
        Me.Label12.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label12.Name = "Label12"
        Me.Label12.Size = New System.Drawing.Size(251, 18)
        Me.Label12.TabIndex = 27
        Me.Label12.Text = "TriShuffle 法からの復号化の結果"
        '
        'DeShuffledByteArrayTextBox
        '
        Me.DeShuffledByteArrayTextBox.BackColor = System.Drawing.SystemColors.ControlDark
        Me.DeShuffledByteArrayTextBox.Location = New System.Drawing.Point(544, 290)
        Me.DeShuffledByteArrayTextBox.Margin = New System.Windows.Forms.Padding(4)
        Me.DeShuffledByteArrayTextBox.Multiline = True
        Me.DeShuffledByteArrayTextBox.Name = "DeShuffledByteArrayTextBox"
        Me.DeShuffledByteArrayTextBox.ReadOnly = True
        Me.DeShuffledByteArrayTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Vertical
        Me.DeShuffledByteArrayTextBox.Size = New System.Drawing.Size(374, 59)
        Me.DeShuffledByteArrayTextBox.TabIndex = 26
        '
        'Label13
        '
        Me.Label13.AutoSize = True
        Me.Label13.Location = New System.Drawing.Point(595, 241)
        Me.Label13.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label13.Name = "Label13"
        Me.Label13.Size = New System.Drawing.Size(26, 18)
        Me.Label13.TabIndex = 25
        Me.Label13.Text = "↑"
        '
        'Label14
        '
        Me.Label14.AutoSize = True
        Me.Label14.Location = New System.Drawing.Point(595, 109)
        Me.Label14.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label14.Name = "Label14"
        Me.Label14.Size = New System.Drawing.Size(26, 18)
        Me.Label14.TabIndex = 24
        Me.Label14.Text = "↑"
        '
        'Label15
        '
        Me.Label15.AutoSize = True
        Me.Label15.Location = New System.Drawing.Point(541, 136)
        Me.Label15.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label15.Name = "Label15"
        Me.Label15.Size = New System.Drawing.Size(286, 18)
        Me.Label15.TabIndex = 23
        Me.Label15.Text = "HeptapleXor 法から復号化した六進数"
        '
        'DeHeptapleXoredTextBox
        '
        Me.DeHeptapleXoredTextBox.BackColor = System.Drawing.SystemColors.ControlDark
        Me.DeHeptapleXoredTextBox.Location = New System.Drawing.Point(544, 158)
        Me.DeHeptapleXoredTextBox.Margin = New System.Windows.Forms.Padding(4)
        Me.DeHeptapleXoredTextBox.Multiline = True
        Me.DeHeptapleXoredTextBox.Name = "DeHeptapleXoredTextBox"
        Me.DeHeptapleXoredTextBox.ReadOnly = True
        Me.DeHeptapleXoredTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Vertical
        Me.DeHeptapleXoredTextBox.Size = New System.Drawing.Size(374, 59)
        Me.DeHeptapleXoredTextBox.TabIndex = 22
        '
        'Label16
        '
        Me.Label16.AutoSize = True
        Me.Label16.Location = New System.Drawing.Point(541, 13)
        Me.Label16.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label16.Name = "Label16"
        Me.Label16.Size = New System.Drawing.Size(258, 18)
        Me.Label16.TabIndex = 21
        Me.Label16.Text = "HexaCrypt 法からの復号化の結果"
        '
        'DecryptedStringTextBox
        '
        Me.DecryptedStringTextBox.BackColor = System.Drawing.SystemColors.ControlDark
        Me.DecryptedStringTextBox.Location = New System.Drawing.Point(544, 35)
        Me.DecryptedStringTextBox.Margin = New System.Windows.Forms.Padding(4)
        Me.DecryptedStringTextBox.Multiline = True
        Me.DecryptedStringTextBox.Name = "DecryptedStringTextBox"
        Me.DecryptedStringTextBox.ReadOnly = True
        Me.DecryptedStringTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Vertical
        Me.DecryptedStringTextBox.Size = New System.Drawing.Size(374, 59)
        Me.DecryptedStringTextBox.TabIndex = 20
        '
        'Label17
        '
        Me.Label17.AutoSize = True
        Me.Label17.Location = New System.Drawing.Point(595, 497)
        Me.Label17.Margin = New System.Windows.Forms.Padding(4, 0, 4, 0)
        Me.Label17.Name = "Label17"
        Me.Label17.Size = New System.Drawing.Size(26, 18)
        Me.Label17.TabIndex = 31
        Me.Label17.Text = "↑"
        '
        'Form1
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(10.0!, 18.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(978, 666)
        Me.Controls.Add(Me.Label17)
        Me.Controls.Add(Me.Label10)
        Me.Controls.Add(Me.Label11)
        Me.Controls.Add(Me.ByteArrayTextBox2)
        Me.Controls.Add(Me.Label12)
        Me.Controls.Add(Me.DeShuffledByteArrayTextBox)
        Me.Controls.Add(Me.Label13)
        Me.Controls.Add(Me.Label14)
        Me.Controls.Add(Me.Label15)
        Me.Controls.Add(Me.DeHeptapleXoredTextBox)
        Me.Controls.Add(Me.Label16)
        Me.Controls.Add(Me.DecryptedStringTextBox)
        Me.Controls.Add(Me.Label9)
        Me.Controls.Add(Me.EncryptResultTextBox)
        Me.Controls.Add(Me.Label5)
        Me.Controls.Add(Me.Label6)
        Me.Controls.Add(Me.Label7)
        Me.Controls.Add(Me.ShuffledByteArrayTextBox)
        Me.Controls.Add(Me.Label8)
        Me.Controls.Add(Me.HeptapleXorTextBox)
        Me.Controls.Add(Me.Label2)
        Me.Controls.Add(Me.Label1)
        Me.Controls.Add(Me.Label4)
        Me.Controls.Add(Me.D6DigitizedTextBox)
        Me.Controls.Add(Me.ExecuteButton)
        Me.Controls.Add(Me.Label3)
        Me.Controls.Add(Me.OriginalStringTextBox)
        Me.Icon = CType(resources.GetObject("$this.Icon"), System.Drawing.Icon)
        Me.Margin = New System.Windows.Forms.Padding(4)
        Me.Name = "Form1"
        Me.Text = "TriCryptTest"
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub
    Friend WithEvents Label3 As System.Windows.Forms.Label
    Friend WithEvents OriginalStringTextBox As System.Windows.Forms.TextBox
    Friend WithEvents ExecuteButton As System.Windows.Forms.Button
    Friend WithEvents Label4 As System.Windows.Forms.Label
    Friend WithEvents D6DigitizedTextBox As System.Windows.Forms.TextBox
    Friend WithEvents Label1 As System.Windows.Forms.Label
    Friend WithEvents Label2 As System.Windows.Forms.Label
    Friend WithEvents Label5 As System.Windows.Forms.Label
    Friend WithEvents Label6 As System.Windows.Forms.Label
    Friend WithEvents Label7 As System.Windows.Forms.Label
    Friend WithEvents ShuffledByteArrayTextBox As System.Windows.Forms.TextBox
    Friend WithEvents Label8 As System.Windows.Forms.Label
    Friend WithEvents HeptapleXorTextBox As System.Windows.Forms.TextBox
    Friend WithEvents EncryptResultTextBox As System.Windows.Forms.TextBox
    Friend WithEvents Label9 As System.Windows.Forms.Label
    Friend WithEvents Label10 As System.Windows.Forms.Label
    Friend WithEvents Label11 As System.Windows.Forms.Label
    Friend WithEvents ByteArrayTextBox2 As System.Windows.Forms.TextBox
    Friend WithEvents Label12 As System.Windows.Forms.Label
    Friend WithEvents DeShuffledByteArrayTextBox As System.Windows.Forms.TextBox
    Friend WithEvents Label13 As System.Windows.Forms.Label
    Friend WithEvents Label14 As System.Windows.Forms.Label
    Friend WithEvents Label15 As System.Windows.Forms.Label
    Friend WithEvents DeHeptapleXoredTextBox As System.Windows.Forms.TextBox
    Friend WithEvents Label16 As System.Windows.Forms.Label
    Friend WithEvents DecryptedStringTextBox As System.Windows.Forms.TextBox
    Friend WithEvents Label17 As System.Windows.Forms.Label

End Class
