def hello(names)
	names.each do|name|
		puts "HELLO, #{name.upcase}"
	end
end

rubies = ['MRI', 'jruby', 'Who knonws']

hello(rubies)
