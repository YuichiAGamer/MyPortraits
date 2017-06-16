require './Dice'

dice = TenSidedDice.new()

20.times do
	puts(dice.roll())
end
