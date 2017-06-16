class FizzBuzz

	def maxNumber=(num) # インスタンス名に"="を付けるのがRubyのsetter
		@maxNumber = num
	end

	def maxNumber # getterとしてインスタンス名と同名を指定
		@maxNumber # ただインスタンス変数を記載するのみでgetter完成
	end

	# インスタンス変数は、通常は以下のような略記法でいく
	attr_accessor :minNumber
	# attr_accessor :(変数名)で、そのインスタンス変数のgetter/setter
	# の一丁あがり

	def initialize(min, max) # Ruby におけるコンストラクタは、 "initialize"
		@minNumber = min
		@maxNumber = max # Rubyのインスタンス変数は @ が付く
	end

	# 参考までに、Ruby にはいわゆるオーバーロードは実装されていないため、
	# コンストラクタの多重定義などはできない模様

	def executeFizzBuzz() # 引数がないなら"()"は略記可
		# minNumber から maxNumber までカウントアップして FizzBuzz
		count = @minNumber

		while(count <= @maxNumber)
			puts("Number is #{count}")
			if (count % 15 == 0)
				puts 'FizzBuzz'
			elsif (count % 5 == 0)
				puts 'Buzz'
			elsif (count % 3 == 0)
				puts'Fizz'
			end
			count+= 1
		end

	end

end

fizzBuzz = FizzBuzz.new(21, 30) # Class.new() でコンストラクタ起動
fizzBuzz.executeFizzBuzz() # 引数を取らないメソッドなら"()"を略記可
