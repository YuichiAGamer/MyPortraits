# サッカーチーム一つぶんをまとめるクラス。
# なお、 Ruby の場合、クラスやメソッドの行の直前に "# ~" を挟めば、
# クラスコメントやメソッドコメントの記法に縛りなどは特にない？
# 気楽に、かつ分かりやすいコメントを入れよう。
class SoccerTeam
  require ".\\soccerMember\\soccerplayer.rb"
  require "csv" # csv ファイルを読むときに便利なライブラリ
  
  # 新規作成時の処理。メンバーを表す配列を初期化
  def initialize
    @members = Array.new
    @memberSlot = {:GK => 0, :DF => 0, :MF => 0, :FW => 0}
    @line_up = {:GKs => [], :DFs => [], :MFs => [], :FWs => []} # "line-up" = 日本語の「スタメン」の意
  end
  
  # サッカーのスタメンの人数。 11 人と定義
  MEMBERS_NUMBER = 11
  
  # attr_accessor :members
  
  # 今回は新規メンバー登録時、
  # 既存のメンバーと同一の背番号ではないかどうか、を調べる処理を仕込みたいので、
  # 通常の attr_accessor :members を利用せず、 getter のみを個別宣言
  
  # メンバーへの参照を返す。なお、この変数は「選手控室」としてみなし、
  # 試合に出ている最中のスタメンはこの中にはいない。
  # また、スタメン発表が行われるたびに、この変数にスタメンが呼び戻される
  def members
    @members
  end
  
  attr_accessor :memberSlot
  
  # 現在出場中のスタメンを返却
  def line_up
    @line_up
  end
  
  # 入力されたサッカーメンバーを自身の members に登録するメソッド。
  # ただし、新規登録選手の背番号が被っていた場合は例外を投げて終了
  def addMember(soccerPlayer)
    if getRegisteredUniNum().include?(soccerPlayer.uniform_num)
      raise(ArgumentError, "Uniform number #{soccerPlayer.uniform_num} is already used")
    else
      @members.push(soccerPlayer)
    end
  end
  
  # 入力されたパスの示す CSV ファイルからデータを読み取り、
  # それを SoccerPlayer のインスタンスとして自身のうちに取り込むメソッド
  def getMembersFromCSV(path)
    # Ruby におけるファイル読み込みはFileクラスを使えばよい。
    # Ruby におけるファイルオープンは、宣言方法が比較的簡単
    # file = File.open(path)
    
    # File.open() / File.close() でも、
    # 相対パスを使うと $LOAD_PATH が読み込み基準になると考えてよいか？
    
    # CSV クラスを使うと、もっと楽になる
    begin
      CSV.foreach(path, col_sep: ", ") do |rows|
        
        name = nil;
        uniform_num = nil;
        positions = nil;
        
        begin
          
          # 1 ~ 3 列目の中に、一つ以上空文字の列があったらスキップ。
          # 三項演算子で空文字判定の表記を簡略化
          is_legal = true
          rows.each do |row|
            is_legal = ((row == "" || row == nil) ? false  : true)          
          end
          unless is_legal
            next
          end
          
          name = rows[0]
          uniform_num = rows[2]
          positions = rows[1].split(" ")
          
          member = SoccerPlayer.new(name, uniform_num, positions)
          self.addMember(member)
        rescue ArgumentError => e # 同一名の登録があった場合のエラーを処理
          puts("#{rows[0]} could not be registered: " + 
            "uniform number #{rows[2]} is already registered")
        end
      end
    rescue NoMethodError => e # foreach() が使えない　≒　row が nil の場合
      # 処理を単純にスキップ
      # puts("No data in this row")
    end

  end
  
  # 次の試合のスタメン 11 人を選出し、
  # 自身のメンバ変数 @line_up に入れるメソッド。
  # 更に、 @line_up を呼び出し元に返却する。
  def determineLineUp()
    
    # まずは出場しているメンバーをいったん控室に呼び戻してリセット
    resetLineUp()
    
    # メンバースロットを確定
    self.determineMemberSlots()
    
    # GK の選定
    @line_up[:GKs] = chooseMember("GK", memberSlot[:GK])
    # DF の選定
    @line_up[:DFs] = chooseMember("DF", memberSlot[:DF])
    # MF の選定
    @line_up[:MFs] = chooseMember("MF", memberSlot[:MF])
    # FW の選定
    @line_up[:FWs] = chooseMember("FW", memberSlot[:FW])
      
    return @line_up    
    
  end
    
  # 現在スタメンになっている選手を全員控室に呼び戻すメソッド
  def resetLineUp()
    
    # puts("Present line up: ")
    # self.announceLineUp()
    # puts("Member length is: #{@members.length}")
    
    for hash in @line_up # こう宣言すると、hash[0]が、key(:GKs, DFs...), hash[1]が 選手一覧
      
      # スタメン選手数が 0 なら強制リターン
      if hash[1].length <= 0 
        next
      end
      
      # スタメンハッシュと控室ハッシュを連結してしまうのが一番簡単な実装か？
      @members.concat(@line_up[hash[0]])
      @line_up[hash[0]].clear      
    end
    
    # puts("Line up is cleared: now line up is: ")
    # self.announceLineUp()
    # puts("Member length is: #{@members.length}")
    
  end

  # ポジション名を第一引数、
  # 選ぶ人数を第二引数に受け、選ばれた選手の配列を返すメソッド
  def chooseMember(position, choosingNumber)
  
    chosenPlayers = Array.new
    chosenNumber = 0
    loop do
      i = rand(0..(@members.length - 1))
      if @members[i].positions.include?(position)
        chosenPlayers << @members[i]
        @members.delete_at(i)
        chosenNumber += 1
  
        if chosenNumber >= choosingNumber
          break
        end
      end
    end
  
    return chosenPlayers
  end
  
     
  # その試行時において、 GK, DF, MF, FW をそれぞれ何人入れるか、
  # をランダムに決定するメソッド。
  # 返るのは[(GK数), (DF数), (MF数), (FW数)] の順で指定された、
  # 長さ 4 のハッシュとする。
  # また、決定後に自身のメンバー数のハッシュを呼び出し元に返す
  def determineMemberSlots()

    # 返却するスロット。連想配列（ハッシュ）で設定する。
    # Ruby における配列は [] で、ハッシュは {} でくくられる。
    # slots = {:GK => 0, :DF => 0, :MF => 0, :FW => 0}
    
    # この段階でシンボルを使うと、仮決定のスロットの結果が本スロットに
    # いきなり反映されてしまい、意図せぬ動作を起こす危険がある。
    # この段階では、仮スロットで試行を行う。
    # 仮スロットのハッシュのキーは、シンボル名を文字列に変換したものを採用
    slots = {"GK" => 0, "DF" => 0, "MF" => 0, "FW" => 0}
  
    
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
      slots["GK"] = 1  
      # DF の人数を最初に決定。 3~5 のランダム人数
      slots["DF"] = rand(3..5)
      # 次に MF の人数を決定。 3~5 のランダム人数
      slots["MF"] = rand(3..5)
      # 最後に FW の人数を、 slots[0] ~ [2] の差分で決定
      slots["FW"] = MEMBERS_NUMBER - slots["GK"] - slots["DF"] - slots["MF"]
      
      # この時点で slots[3] の値が 1 ~ 3 に収まっていれば、
      # break して return。そうでないなら、もう一周ループ
      if(slots["FW"] >= 1 && slots["FW"] <= 3)
        break
      end
  
    end
    
    # この時点での仮スロットの値を本スロットに適用して返却
    @memberSlot[:GK] = slots["GK"]
    @memberSlot[:DF] = slots["DF"]
    @memberSlot[:MF] = slots["MF"]
    @memberSlot[:FW] = slots["FW"]
  
    return @memberSlot
  end
  
  # 現在のスタメンの各ポジションの枠数を告げるメソッド
  def announceMemberSlot()
    puts("Today's line-up will be announced!")
    puts("GK: #{@memberSlot[:GK]}, DF: #{@memberSlot[:DF]}, " +
    "MF: #{@memberSlot[:MF]}, FW: #{@memberSlot[:FW]}")
  end
  
  # 現在のスタメンを、ポジションごとに読み上げるメソッド
  def announceLineUp()
    puts("GK is...")
    @line_up[:GKs].each do |player|
      puts("Uniform number #{player.uniform_num}, " + 
        "#{player.name}, who does #{player.positions}")
    end
    
    puts("DF are...")
    @line_up[:DFs].each do |player|
      puts("Uniform number #{player.uniform_num}, " + 
      "#{player.name}, who does #{player.positions}")
    end
    
    puts("MF are...")
    @line_up[:MFs].each do |player|
      puts("Uniform number #{player.uniform_num}, " + 
      "#{player.name}, who does #{player.positions}")
    end
    
    puts("FW is/are...")
    @line_up[:FWs].each do |player|
      puts("Uniform number #{player.uniform_num}, " + 
      "#{player.name}, who does #{player.positions}")
    end
    
  end
  
  # このサッカーチームの中で未使用である背番号のうち、
  # 最も若い番号を返却するメソッド。
  # 二分木探索法を応用した、「二分木欠番捜索法」というアルゴリズムを利用する
  def getAvailableUniNum()
    usedNumbers = self.getRegisteredUniNum()
    usedNumbers.sort_by!{|item| item.to_i} # 二分木探索にはソートが必須
      
    availableNum = self.getLackedHalfOfBTree(usedNumbers)
    
    # もし上記アルゴリズムで返却値無しなら、
    # 欠番は存在しないものとして、使われた値の最高値 + 1 の値を設定
    if availableNum == nil
      availableNum = usedNumbers[-1] + 1
    end
  
  return availableNum
    
  end
  
  # 入力された数列を二分割し、欠番の存在する側のグループを返却するメソッド。
  # もし二分割したグループの両方に欠番が存在する場合、
  # 小さい値のグループを返却する。
  # なお、入力された数列は昇順にソートされた整数型で、
  # 欠番を除いては 1 刻みに数値が増えることを想定している。
  # もし数列の長さが 1 であるか、欠番がない場合は、 nil を返却する
  def getLackedHalfOfBTree(array)
    
    # 数列の長さが 1 の場合は欠番そのものが存在していないとみなし nil
    if array.length == 1
      return nil
    end
    
    # 数列の長さが 4 以上なら、
    # [0]~[配列長さ÷2 - 1], [配列長さ÷2]~[配列長さ - 1] の 2 グループに分割。
    # なお、端数は切り捨て
    former_arr = array.slice(0..(array.length / 2 - 1))
    latter_arr = array.slice((array.length / 2)..(array.length - 1))
    
    # 入力された数字は 1 刻みで増えていることが想定されるので、
    # それぞれの数列の最後の値は、 長さ - 1 ぶん増えていることが期待される。
    # もし欠番があれば、この計算が等しくなくなる。
    # この性質を利用して、欠番を捜索する。
    # なお、欠番が former_arr と latter_arr のちょうど真ん中にある可能性にも注意
    
    # 小さい値の側の半分    
    if ((former_arr[-1]) != (former_arr[0] + former_arr.length - 1))
      # こちらに欠番があれば、こちらの半分を利用して自身を再帰呼び出し
      return getLackedHalfOfBTree(former_arr)
    # 欠番が former_arr と latter_arr のちょうど真ん中にある場合、その中間値を返却
    elsif ((latter_arr[0] - former_arr[-1]) != 1)
      return (latter_arr[0] + former_arr[former_arr.length - 1]) / 2
    # 大きい側の半分
    elsif ((latter_arr[-1]) != (latter_arr[0] + latter_arr.length - 1))
      # こちらに欠番があれば、こちらの半分を利用して自身を再帰呼び出し
      return getLackedHalfOfBTree(latter_arr)
    else
      # 上記のいずれにも欠番がなければ、 nil を返却
      return nil
    end
  end
  
  # 上記欠番発見アルゴリズムでは、欠番が複数存在するなら若い番号を優先、
  # という動作をさせる際、「小さい側の半分」「分けた半分のちょうど真ん中」
  # 「大きい側の半分」という順番で、欠番探索を行っていくのがミソ
  
  # private # こう宣言すると、ここから下は外部からのアクセスを禁止する
  
  # 現時点でチームメンバーが使っている背番号の配列を返す。
  # 出場中のメンバーの分までカウントアップ
  def getRegisteredUniNum
    uniformNums = Array.new()
    @members.each do |member|
      uniformNums.push(member.uniform_num.to_i)
    end
    
    for hash in @line_up # hash[0] は シンボル
      @line_up[hash[0]].each do |member|
        uniformNums.push(member.uniform_num.to_i)
      end
    end
    
    return uniformNums
  end
  
end