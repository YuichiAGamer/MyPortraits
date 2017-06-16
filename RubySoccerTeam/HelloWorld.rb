require "./TestClass"

puts 'Hello, world'
bar = Array.new

bar.push("hoge")
bar.push("fuga")
puts bar

baz = TestClass.new() # Ruby では同階層のクラスでも require は必須
baz.say_hello()