require ".\\soccerMember\\soccerplayer.rb"

MEMBERS_NUMBER = 11

# その試行時において、 GK, DF, MF, FW をそれぞれ何人入れるか、
# をランダムに決定するメソッド。
# 返るのは[(GK数), (DF数), (MF数), (FW数)] の順で指定された、
# 長さ 4 のハッシュとする
def determineSlots()

	# 返却するスロット。連想配列（ハッシュ）で設定する。
	# Ruby における配列は [] で、ハッシュは {} でくくられる。
	slots = {:GK => 0, :DF => 0, :MF => 0, :FW => 0}

	# なお、 Ruby におけるハッシュのベストプラクティスは、
	# キーにシンボル(":(変数名)")を使うこと。
	# シンボルは Ruby において、内部的に一意の整数として利用される。
	# (文字列の皮を被った整数と考えてよい)
	# そしてハッシュのキーにシンボルを使うと、内部的にキー検索を行う際、
	# 文字列リテラルをキーとして利用するのに比べ、パフォーマンスが
	# 向上する。 Ruby で大量のデータをハッシュで管理するときは、
	# 是非とも留意されたい。

	loop do
	
		# GK の人数は 1 名で固定
		slots[:GK] = 1

		# DF の人数を最初に決定。 3~5 のランダム人数
		slots[:DF] = rand(3..5)

		# 次に MF の人数を決定。 3~5 のランダム人数
		slots[:MF] = rand(3..5)

		# 最後に FW の人数を、 slots[0] ~ [2] の差分で決定
		slots[:FW] = MEMBERS_NUMBER - slots[:GK] - slots[:DF] - slots[:MF]
		
		# この時点で slots[3] の値が 1 ~ 3 に収まっていれば、
		# break して return。そうでないなら、もう一周ループ
		if(slots[:FW] >= 1 && slots[:FW] <= 3)
			break
		end

	end

	return slots
end


# 選手名簿を第一引数、ポジション名を第二引数、
# 選ぶ人数を第三引数に受け、選ばれた選手の配列を返すメソッド
def chooseMember(soccerPlayers, position, choosingNumber)

	chosenPlayers = Array.new
	chosenNumber = 0
	loop do
		i = rand(0..(soccerPlayers.length - 1))
		if soccerPlayers[i].positions.include?(position)
			chosenPlayers << soccerPlayers[i]
			soccerPlayers.delete_at(i)
			chosenNumber += 1

			if chosenNumber >= choosingNumber
				break
			end
		end
	end

	return chosenPlayers
end

# 選手の名前を入れる変数を用意
soccerPlayers = Array.new

# Ruby におけるファイル読み込みはFileクラスを使えばよい。
# Ruby におけるファイルオープンは、宣言方法が比較的簡単
file = File.open('.\\soccerMember\\soccerMembers.csv')

# File.open() でも、
# 相対パスを使うと $LOAD_PATH が読み込み基準になると考えてよいか？

# File#each を使うと、読み込んだファイルを一行ずつ読める
file.each do |line|

	# 読み込んだ行について、 "," ごとに切り分ける
	tokens = line.split(",")
	
	# 適正なデータであるかどうかのフラグ
	is_legal = true

	tokens.each do |token|
		token.strip! # "!" の付いた破壊的メソッドで自身の半角スペースを除去
		token.gsub!(/\"/, '') # 同じく破壊的メソッドで自身の '"' を除去a

		# 結果が空文字なら、このデータは不正なものとみなしてスキップフラグ
		if (token == '' || token == nil)
			is_legal = false
		end
	end

	# データが不正でないなら、それを利用してインスタンス作成
	if is_legal
		name = tokens[0]
		tokens.delete_at(0)

		positions = tokens
		soccerPlayers.push(SoccerPlayer.new(name, positions))
	end
end

file.close

# イレブンの内訳を決定
positionSlots = determineSlots()

puts("Today's starting member will be announced!")
puts("GK: #{positionSlots[:GK]}, DF: #{positionSlots[:DF]}, " +
"MF: #{positionSlots[:MF]}, FW: #{positionSlots[:FW]}")
puts()

gks = chooseMember(soccerPlayers, "GK", positionSlots[:GK])
dfs = chooseMember(soccerPlayers, "DF", positionSlots[:DF])
mfs = chooseMember(soccerPlayers, "MF", positionSlots[:MF])
fws = chooseMember(soccerPlayers, "FW", positionSlots[:FW])

puts("GK is...")
gks.each do |player|
	puts("#{player.name}, who does #{player.positions}")
end
puts()

puts("DF are...")
dfs.each do |player|
	puts("#{player.name}, who does #{player.positions}")
end
puts()

puts("MF are...")
mfs.each do |player|
	puts("#{player.name}, who does #{player.positions}")
end
puts()

puts("FW is/are...")
fws.each do |player|
	puts("#{player.name}, who does #{player.positions}")
end

