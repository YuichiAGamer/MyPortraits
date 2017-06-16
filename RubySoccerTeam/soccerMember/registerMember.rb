# CSV ファイル形式のサッカー選手名簿に、
# 新しく選手を書き加えるスクリプト。
# チーム名がそのまま CSV ファイルの名称となり、
# CSV ファイル内の行は、 2~3 列からなるとする。
# e.g) "Alice Anderson", "GK"

# 選手の最高ポジション数
MAX_POSITIONS = 2

puts("サッカー選手の名簿登録を行います。" + 
"登録先のチーム名を入力してください。")

# Ruby で標準入力を受け取るのは簡単。 gets メソッドを使うだけ。
# ただし、そのままだと改行記号が混ざるので、破壊的メソッド chomp! で
# 末尾の \n を切り落とすこと。
team = gets.chomp!

puts("チーム名は \"" + team + "\" です。")

# "(team).csv" という名前の CSV ファイルを追記モードでオープン。
# 追記モードで開けば、ファイルが未存在でも自動的に新規作成される
namelist = File.open("#{team}.csv", "a")

puts(team + "に新規登録する選手の名前を入力してください。\n")
name = gets.chomp!

positions = Array.new
is_multiple = false

loop do

	puts(name + "選手のポジションを入力してください。(最高 " +
	MAX_POSITIONS.to_s + " ポジションまで)")
	if(is_multiple)
		puts("(0.入力を終了する 1.GK 2.DF 3.MF 4.FW " + 
		"9.ポジション入力をリセット)")
		puts("現在入力済みのポジション: " + positions.join(", "))
	else
		puts("(1.GK 2.DF 3.MF 4.FW)")
	end

	position = gets.chomp!
	case position
	when "0"
		unless(is_multiple)
			puts("無効な文字入力です。やり直してください。\n")
			next
		else
			break
		end
	when "1"
		unless positions.include?("GK")
			positions.push("GK")
		else
			puts("そのポジションはすでに登録済みです\n")
			next
		end
	when "2"
		unless positions.include?("DF")
			positions.push("DF")
		else
			puts("そのポジションはすでに登録済みです\n")
			next
		end
	when "3"
		unless positions.include?("MF")
			positions.push("MF")
		else
			puts("そのポジションはすでに登録済みです\n")
			next
		end
	when "4"
		unless positions.include?("FW")
			positions.push("FW")
		else
			puts("そのポジションはすでに登録済みです\n")
			next
		end
	when "9"
		positions.clear
		is_multiple = false
		next
	else
		puts("無効な文字入力です。やり直してください。\n")
		next
	end
	
	# 処理がここまで届いたら複数個入力と判断
	is_multiple = true

	# この時点で最大ポジション数に達していたら脱出
	if (positions.length >= MAX_POSITIONS)
		break
	end

end

puts("以下の内容で選手を登録します。よろしいですか？[y/n]")
puts("名前: " + name + " ポジション: " + positions.join(", "))
answer = gets.chomp!

unless (answer == "y")
	puts("選手登録を中止しました。プログラムを終了します。")
	namelist.close
	exit true
else
	# 入力されたデータを CSV に書き込む
	namelist.write("\""+ name + "\"")
	namelist.write(", " + "\"" + positions.join(" ") + "\"")
	namelist.write("\n")
end

puts(name + " 選手の登録に成功しました。プログラムを終了します。")
namelist.close
exit true

