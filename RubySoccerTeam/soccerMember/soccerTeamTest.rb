# Windows におけるファイルセパレータ "\" は、エスケープして "\\" としないと、
# ディレクトリ構造を正しく宣言できないので注意
require ".\\soccerMember\\soccerplayer.rb"
require ".\\soccerMember\\SoccerTeam.rb"

soccerTeam = SoccerTeam.new

# puts($LOAD_PATH) で、 require において読み出される先のディレクトリを確認
soccerTeam.getMembersFromCSV(".\\soccerMember\\soccerMembers.csv")

#soccerTeam.determineLineUp()
#soccerTeam.announceMemberSlot()
#soccerTeam.announceLineUp()
#
#soccerTeam.determineLineUp()
#soccerTeam.announceMemberSlot()
#soccerTeam.announceLineUp()

array = [1, 2, 3, 4, 5, 6, 7, 9, 10]
puts(soccerTeam.getLackedHalfOfBTree(array))
puts(soccerTeam.getAvailableUniNum())