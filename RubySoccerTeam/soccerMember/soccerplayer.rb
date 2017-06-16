class SoccerPlayer

	attr_accessor :name
	attr_accessor :positions
	attr_accessor :uniform_num

	def initialize(name, uniform_num, *positions) # 可変長引数は通常引数の次
		@name = name
		@uniform_num = uniform_num
    @positions = positions.flatten! # 文字列配列を展開
	end

end
