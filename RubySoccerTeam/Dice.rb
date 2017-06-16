# サイコロの抽象クラス。
# 厳密に言えば、 Ruby は言語仕様上抽象クラスをサポートしていないが、
# 「継承先クラスで面数を定義しないとまともに動かないクラス」として
# このクラスを定義しているので、便宜的に「抽象クラス」と呼称する。
# このクラスを継承して、4 面体、 6 面体、 8 面体、 10 面体、
# 12 面体、 20 面体のサイコロをそれぞれ作成してみる
class AbstractDice

	# "attr_accessor :(変数名)" ではなく "attr_reader :(変数名)" と
	# 宣言すると、その変数を読み込み専用とすることができる。
	# サイコロは一度作られたら面数を変更できないことの再現
	attr_reader :sides

	# デフォルトでは面数は nil(Ruby の null)。
	# 各サイコロを継承するとき、インスタンス変数 @sides を定義すること。
	# これを直接インスタンス化して利用すると、 roll() メソッドが引数不正で
	# エラーを起こす
	def initialize()
			puts("AbstractDice initializing...")
			@sides = nil
	end

	def roll()
		return rand(1..@sides)
	end

	def doCriticalHit()
		puts("Critical hit!")
	end

end

# 4 面体サイコロのクラス。 ruby でクラス継承を行うときの記法は、
# "class (子クラス名) < (親クラス名)"。
class FourSidedDice < AbstractDice

	# 面数の定義。 ruby においては、
	# 子クラスのコンストラクタから親クラスのコンストラクタは自動実行されない。
	# 親クラスのコンストラクタを実行したいなら、
	# 明示的に super() を宣言すること。
	# なお、親クラスと子クラスのコンストラクタのシグネチャは、
	# それぞれ異なっていても構わない。
	def initialize()
		# super()
		puts("FourSidedDice initializing...")
		@sides = 4
	end

end

# 6 面体サイコロのクラス
class SixSidedDice < AbstractDice

	def initialize()
		puts("SixSidedDice initializing...")
		@sides = 6
	end
	
end

# 8 面体サイコロのクラス
class EightSidedDice < AbstractDice

	def initialize()
		puts("EightSidedDice initializing...")
		@sides = 8
	end
	
end

# 10 面体サイコロのクラス。
# roll() メソッドをオーバーライドし、 10 の目が出た場合
# クリティカルヒットとする
class TenSidedDice < AbstractDice

	def initialize()
		puts("TenSidedDice initializing...")
		@sides = 10
	end

	# 親メソッドをオーバーライドし、クリティカル機能を実装
	def roll()
		result = rand(1..@sides)
		if (result == 10)
			doCriticalHit()
		end
		return result
	end
	
end

# 12 面体サイコロのクラス
# roll() メソッドをオーバーライドし、 12 の目が出た場合
# クリティカルヒットとする
class TwelveSidedDice < AbstractDice

	def initialize()
		puts("TwelveSidedDice initializing...")
		@sides = 12
	end
	
	# 親メソッドをオーバーライドし、クリティカル機能を実装
	def roll()
		result = rand(1..@sides)
		if (result == 12)
			doCriticalHit()
		end
		return result
	end
end

# 20 面体サイコロのクラス
# roll() メソッドをオーバーライドし、 19 or 20 の目が出た場合
# クリティカルヒットとする
class TwentySidedDice < AbstractDice

	def initialize()
		puts("TwentySidedDice initilizing...")
		@sides = 20
	end
	
	# 親メソッドをオーバーライドし、クリティカル機能を実装
	def roll()
		result = rand(1..@sides)
		if (result >= 19)
			doCriticalHit()
		end
		return result
	end
end

