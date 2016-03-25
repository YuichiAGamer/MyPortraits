package com.cosmicbarrage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Cosmic Barrage�@�ɂ����āA�G�L�����S�ʂ̃f�[�^�Ɠ�����Ǘ�����N���X�B<br>
 * ���p���ɂ́A�G��̂ɂ����̃N���X�̃C���X�^���X������錾���A<br>
 * ������e�N���X�̃R���N�V�����Ɋi�[���ĉ^�p���邱�ƁB<br><br>
 * <b>���t�@�N�^�����O(2012/12/17)</b><br>
 * ���̃N���X�́AArrayList���p������EnemyHive�N���X�Ƀ��b�v���Ďg��������<br>
 * ���t�@�N�^�����O�BImageIndexChanger�X���b�h�́AEnemyHive�X���b�h�ɈڐA�B<br>
 * ����Enemy�N���X�����s����X���b�h�́A�j�󎞂�BlastImageThread�݂̂ɕύX�B
 * @author Yuichi Watanabe
 * @since 2012/12/10
 * @version 1.1.0 Code Refactored on 2012/12/17
 * */
public class Enemy{

	/* *
	 * �t�B�[���h(�����o�ϐ�)
	 * */
	
	//���g�̎��̎��ʗpID
	int myID;
	
	//�e�t�H�[���ւ̎Q��
	private CosmicBarrage parentFrame;
	
	//���̓G�̃O���t�B�b�N
	BufferedImage enemyImage;
	
	//���̓G�����U����O���t�B�b�N
	BufferedImage blastImage;
	
	//���̏u�Ԃɂ����āA�G�O���t�B�b�N�̉����ڂ�\�����邩�������C���f�b�N�X�B
	//0~3�̂����ꂩ�̒l���Ƃ�
	int imageIndex = 0;
	
	//imageIndex�̒l�ɉ����Č��܂�AenemyImage�̕\���̈�
	Rectangle displayingRegion = new Rectangle(0, 0, 32, 32);
	
	//imageIndexChanger��EnemyHive�ɈڐA�����̂ŁA�����o�^���폜
	
	//���A�����Ax�Ay�A���W�l�_�Z�b�g
	int width;// = 32;
	int height;// = 32;
	int x;// = 0;
	int y;// = 0;
	
	//�����蔻�肪���\����艽�s�N�Z�����������������I�t�Z�b�g�B
	//�f�t�H���g�l�́u2�v���w��B
	//�Ȃ��A"Hit Checking"�́u�����蔻��v�̉p��Ƃ��č̗p�����^�[��
	int hitCheckingOffset = 2;
	
	//���̓G���u�����āv���邩�ǂ����������t���O�B���̃t���O��true�ł���ԁA
	//�G�O���t�B�b�N�̕`��͑����B�܂��A���ꂪfalse�ł���ꍇ�A�����蔻�������
	boolean isAlive = true;
	
	//�X�R�A�\������������̂ŁAdigitManager�N���X�̌^���錾
	DigitManager digitManager;
	
	/**
	 * �R���X�g���N�^
	 * */
	Enemy(CosmicBarrage parentFrameInput, int x, int y, Integer graphicID, int namedID){
		//�e�N���X�̎Q�ƃQ�b�g
		parentFrame = parentFrameInput;
		
		//���g��ID���m��
		this.myID = namedID;
		
		//�e�탁���o�ϐ����A�����Ŗ����I�ɒl��^����
		width = 32;
		height = 32;
		this.x = x;
		this.y = y;
		hitCheckingOffset = 1;
		digitManager = new DigitManager();
		
		
		//����graphicID����A�G�O���t�B�b�N�̃��\�[�X�ɃA�N�Z�X���镶������쐬
		String resourcePath = "Enemy" + graphicID.toString() + ".png";
		
		//��ō쐬������������A�N�Z�X�L�[�ɂ��āA�G�C���[�W�����\�[�X����Q�b�g
		enemyImage = null;		
		try {
			enemyImage = ImageIO.read(getClass().getResource(resourcePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//imageIndexChanger�́AEnemyHive�N���X�ɈڐA
		
		
	}//�R���X�g���N�^�I��
	
	/*
	 * �ȉ��A���\�b�h����ѓ����N���X��񋓂���B
	 * �񋓂��鏇�Ԃ�
	 * 1.�C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h
	 * 2.�ʏ�̃��\�b�h
	 * 3.�����N���X
	 * 4.�f�X�g���N�^���\�b�h
	 * �̏��Ƃ���B
	 * 1,2,3�̃O���[�v���ł́A���ꂼ��X��ABC���Ń����o������ł���B
	 * �I�[�o�[���[�h���ꂽ���\�b�h�́A���������Ȃ������瑽�����ɕ��ׂ�
	 * */
	
	/* ##############################
	 * 1.�C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h
	 * ##############################*/	
	
	//�G�C���[�W�̕`���EnemyHive�N���X�Ɉ�C�����̂ŁA
	//EnemyHive�N���X�ɃR�[�h���ڐA�ς�
	
	/* ############
	 * 2.�ʏ�̃��\�b�h
	 * ############*/

	/**
	 * ����enemy�C���X�^���X���j�󂳂ꂽ�Ƃ��ɋN������郁�\�b�h�B
	 * ���̃��\�b�h���N�������u�ԂɁA���̃N���X��isAlive��false�ƂȂ�A
	 * �ʏ�̃O���t�B�b�N�ł͂Ȃ������̃G�t�F�N�g���`�悳���B
	 * ���̔����̃G�t�F�N�g��30�t���[�������A�Ō��1�t���[�����`�悳��I�����u�ԁA
	 * �C���X�^���X��enemyList����O����A���g���j�������
	 * */
	public void breakDown(){
		
		//isAlive��false�ɃZ�b�g
		isAlive = false;
		
		//�����C���[�W�̕`���ʃX���b�h�ŊJ�n�B
		//�Ȃ��A���̃��\�b�h�̏������e�͑��̓����N���X�ɐ؂�o��
		Thread blastImageThread = new BlastImageThread();
		blastImageThread.start();
		
	}//breakDown���\�b�h�I��
	
	/**���̓G�̒��S���W��ԋp���郁�\�b�h*/
	public Point getCenterLocation(){
		int centerX = this.x + this.width;
		int centerY = this.y + this.height;
		Point point = new Point(centerX, centerY);
		
		return point;
	}
	
	/* ##########
	 * 3.�����N���X
	 * ##########*/
	
	/**
	 * �����C���[�W�̕`���S�����鏈���N���X�B
	 * �Ȃ��A���̃N���X�̏������Ō�܂Ői�񂾂Ȃ�A
	 * �C���X�^���X�̃f�X�g���N�^���N�����āA�C���X�^���X���ۂ��Ɣj�������
	 * */
	class BlastImageThread extends Thread{
		
		//���S�n���W
		private List<Point> blastPoints = new ArrayList<Point>();
		//�������a�B�Ȃ��AList�̃W�F�l���b�N�ɂ͊�{�^�̐錾�͂ł��Ȃ��̂Œ���
		private List<Integer> blastRadiuses = new ArrayList<Integer>();
		//����
		private Random random = new Random();		
		
		@Override
		public void run(){
			
			//30�t���[���ԁA�������J��Ԃ�
			for(int frameNumber = 1; frameNumber <= 30; frameNumber++) {
				
				//blastImage��������
				blastImage = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
				Graphics g = blastImage.getGraphics();
				
				/*���ɁA������`��B�����̃O���t�B�b�N�̃A���S���Y���͈ȉ��̒ʂ�B
				 * 1.�����̃O���t�B�b�N�́A1�t���[�����Ƃɔ��a��1���傫���Ȃ锒���~�ł���
				 * 2.�G�̔����G�t�F�N�g�ɂ����鎞�Ԃ�30�t���[���B
				 * ������ɂ�����t���[������8�t���[���Ƃ���B�܂�A������������鐡�O�́A
				 * �����̔��a��8�s�N�Z��
				 * 3.�������n�܂����ŏ���1�t���[���ځA�y�эŏI�t���[���Ŕ������҂�����I������
				 * 22�t���[���ځA����2�t���[���ł͕K�������𔭐�������
				 * 4.�܂��A22�t���[���ڂ܂ł̊e�t���[���ɂ����Ă��A�V����������1/4�̊m����
				 * ����������̂Ƃ���
				 * 5.������̃t���[���ɂ����Ă��A��������������ꍇ�A(8, 8)~(40, 40)�̂����ꂩ��
				 * ���W�������_���ɑI�΂�A���������S�n�ƂȂ�*/
				
				/* ###############
				 * �����̏�񏈗���
				 * ###############*/
				
				//�܂���List�Ɋ܂܂�Ă��锚���̔��a�����ꂼ�ꂷ�ׂ�+1
				for (int i = 0; i < blastRadiuses.size(); i++) {
					blastRadiuses.set(i, blastRadiuses.get(i) + 1);
				}
				
				//���̏�ŁA���̏�Ԃ�List�̒�����AblastRadiuses��8���z���Ă�����̂��������ꍇ�A
				//���̃C���f�b�N�X���������폜
				for (int i = 0; i < blastRadiuses.size(); i++){
					if (blastRadiuses.get(i).intValue() > 8) {
						blastPoints.remove(i);
						blastRadiuses.remove(i);
					}				
				}
				
				/*�����āA���݂̃t���[���ŁA�V���Ȕ����_��ݒ肷�邩�ǂ������߂�B
				1�t���[���ڂ�22�t���[���ڂ͕K�������𔭐������A23�t���[���ڈȍ~�͔����𔭐������Ȃ��B
				2~21�t���[���ڂł́A1/4�̊m���Ń����_���ɔ���������*/
				if ( (frameNumber == 1) || (frameNumber == 22) ) {
					//���W�������_���Ɍ���
					int x = random.nextInt(33) + 8;
					int y = random.nextInt(33) + 8;
					
					//���̍��W�f�[�^���AList�ɒǉ�
					blastPoints.add(new Point(x, y));
					blastRadiuses.add(1);
				}else if ((frameNumber >= 2) && (frameNumber <= 21) &&
						  (random.nextInt(4) == 0) ) {
					//���W�������_���Ɍ���
					int x = random.nextInt(33) + 8;
					int y = random.nextInt(33) + 8;
					
					//���̍��W�f�[�^���AList�ɒǉ�
					blastPoints.add(new Point(x, y));
					blastRadiuses.add(1);
				}
				
				/* ###########
				 * �����̕`���
				 * ###########*/
				
				//��L�̔����̏�񏈗����I�����Ȃ�A����List�Ɋi�[����Ă��锚����
				//�������ɁA������`��
				
				for(int i = 0; i < blastPoints.size(); i++) {
					//�����������~�̕`��N�_���A�����̔������W-�������a�Ōv�Z
					int x = blastPoints.get(i).x - blastRadiuses.get(i);
					int y = blastPoints.get(i).y - blastRadiuses.get(i);
					
					//�����́A�܂������~��`�悵�āA���̎��͂��������ŉ���邱�Ƃŕ`�悳���
					g.setColor(Color.WHITE);
					g.fillOval(x, y, blastRadiuses.get(i) * 2, blastRadiuses.get(i) * 2);
					g.setColor(Color.BLACK);
					g.drawOval(x, y, blastRadiuses.get(i) * 2, blastRadiuses.get(i) * 2);
					
				}
				
				//�����ɁA�X�R�A���\�����Ă݂�
				/*BufferedImage scoreImage = digitManager.getDigitGraphic(100, DigitFontOption.HARD);
				//���̃t���[���ŃX�R�A��\������ׂ��n�_���W���v�Z
				int scoreX = (48 - scoreImage.getWidth()) / 2;
				int scoreY = (48 - scoreImage.getHeight()) - frameNumber;
				
				g.drawImage(scoreImage,
							scoreX, scoreY,
							scoreX + scoreImage.getWidth() * 1, scoreY + scoreImage.getWidth() * 1, 
							0, 0, scoreImage.getWidth(), scoreImage.getWidth(),
							parentFrame);*/
				
				//�Ō�ɁA1�t���[���Ԃ񂾂��X���[�v
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}//for�ߏI��
			
			//�����̕`�悪�I������Ȃ�AblastImage��Null�ɂ������
			//�f�X�g���N�^���N��
			blastImage = null;
			destructEnemy();
		}
	}

	/* ###############
	 * 4.�f�X�g���N�^���\�b�h
	 * ###############*/
	
	/**
	 * ���̃C���X�^���X���g��j�󂷂郁�\�b�h�B<br>
	 * ��̓I�ɍs���鏈���͈ȉ��̒ʂ�B<br><br>
	 * 1.���g��isAlive�t���O��false�ɃZ�b�g<br>
	 * 2.�e�N���X��enemyList����A���g�̓o�^��remove����<br>
	 * 3.�e�N���X��drawEventManager����A���g�̓o�^���O��<br>
	 * 4.�����Ɏ��g�̎Q�Ƃ�null�ɂ���<br>
	 * 5.�Ō��GC���蓮�N�����A���S�ɃC���X�^���X�����ł�����
	 * */
	void destructEnemy() {
		
		//isAlive�t���O��false�ɂ���
		isAlive = false;
		
		//�e�N���X��enemyList����A���g��T���o���B���̍�ID���L�[�Ƃ���B
		//���g��\��enemyList�̗v�f����������A�����j������B
		
		//�܂���ID���玩�g�̎Q�Ƃ��m��
		Enemy myself = null;
		for (int i = 0; i < parentFrame.enemyHive.size(); i++) {
			if (parentFrame.enemyHive.get(i).myID == myID) {
				myself = parentFrame.enemyHive.get(i);
				break;//�Q�Ƃ��m�肵���Ȃ炻��ȏ㑱���Ȃ��Ă悢�̂�break
			}
		}
		
		//myself��enemyList�ォ��j��
		parentFrame.enemyHive.remove(myself);
		
		//���g�ւ̎Q�Ƃ�null�ɂ���
		myself = null;
		
		//�Ō�ɁAGC���蓮�N��
		System.gc();
	}
	
	
	
}//Enemy�N���X�I��

/**
 * �]�k�Ȃ���A����N���X���̏����̓s����Thread�N���X�̓����N���X�△���N���X��錾�����ہA�Q�Ƃɒ��ӁB
 * �X���b�h�𔭍s�����N���X���ŁA�����N���X������N���X�����A���̒���"this"�g�[�N�����g���ƁA
 * ����"this"���Q�Ƃ���̂́A�X���b�h�𔭍s�����N���X�ł͂Ȃ��A�����N���X������N���X���̂��́B
 * ���̂��߁A�X���b�h���s���N���X�̕ϐ���I�u�W�F�N�g���Q�Ƃ���ꍇ�A"this"���g�킸�A
 * ���ڕϐ���I�u�W�F�N�g�̖��O���������ނ��ƁB
 * 
 * ���s���N���X�Ɩ���/�����N���X�Ŏ��ʂ������O�̕ϐ����g���Ă���A
 * ���s���N���X�̃����o���Ɩ���/�����N���X�̃����o��������킵���c�c�Ȃǂ̗��R�ŁA
 * �ǂ����Ă�"this"�����̎Q�ƃg�[�N�����g�������ꍇ�́A
 * �X���b�h���s���̃N���X�̎Q�Ƃ𖾎��I�ɓn���Ƃ��������􂪂���B
 * Thread�N���X���p�����������N���X���C���X�^���X��������ہA
 * ���̓����N���X�ɔ��s���N���X�̎Q�Ƃ����b�v����`�ŁA�^�錾���s���B
 * (�Ⴆ��"ParentClass parentClassType = parentClassReference;"�Ƃ����Q�ƌ^��錾����Ȃ�)
 * ���̌^�錾�̂̂��A�ϐ���run���\�b�h���Ŏg���΁A�����I�ɃX���b�h�̔��s���N���X�̎Q�Ƃ��s�����Ƃ��ł���B
 * (��:parentClassType.parentClassMember,parentClassType.parentClassMethod()�Ȃ�)
 * 
 * ���������̕��@�͎኱�J�͂�������A�킸���Ȃ���ǐ���������B
 * �܂��A�^�錾�����b�v���Ȃ���΂����Ȃ��Ƃ���������l����ƁA�����N���X�Ƃ͑���������������ł���B
 * �i�����N���X�͒ʏ�A�����N���X�ɐ؂�o���قǂł͂Ȃ��傫���̏������������߂̃R�[�f�B���O�Z�p�ł���B
 * �܂�A�^�錾�����b�v���Ȃ���΂����Ȃ��قǋK�͂̑傫���������K�v�Ȃ�A
 * ������������͖����N���X�̏o�開�ł͂Ȃ��j
 * 
 * Thread�N���X���p�����������N���X����K�͂ɂȂ�̂ŁA
 * �X���b�h���s���̃N���X�̃\�[�X�R�[�h�ƕ������A�ʂ̃N���X�̃\�[�X�R�[�h�Ƃ��ċL�q�����������A
 * �ێ琫�E�ǐ��������Ȃ�c�c
 * �Ȃǂ̎���Ȃ�����A�킴�킴�e�N���X�̎Q�Ƃ̌^�錾�𖾎��I�ɍs�����炢�Ȃ�A
 * �����N���X�̕ϐ�����I�u�W�F�N�g�����H�v���������A�����I���ȕւȕ���ł���B
 * 
 * */

/**
 * Java�̃N���X�̃����o�������ɂ���Tips�B
 * Java�ɂ����āA�N���X�̃����o�ϐ��̏����l�ݒ��A�����o�I�u�W�F�N�g�̏����C���X�^���X���́A
 * ���̓�̂ǂ��炩�A�������͗����̕��@�ōs�����Ƃ��ł���B
 * 
 * 1.�����o�ϐ�/�I�u�W�F�N�g�̌^�錾���s���ہA�����ɐ錾���s��
 * 2.���̃N���X�̃R���X�g���N�^�ŏ����l��^����/�C���X�^���X�����s��
 * 
 * ���̓��A1�̕��@��������ꍇ�́A�R���X�g���N�^������ɏ����l�ݒ�/�C���X�^���X�����\�ł���B
 * ���������̏ꍇ�A�����l�ݒ�/�C���X�^���X�����s����^�C�~���O�͕s�m��ł���B
 * ��̓I�^�C�~���O�������ċc�_������Ȃ�΁A
 * ���̃N���X�̃C���X�^���X�ɃA�N�Z�X���s���A���̕ϐ���I�u�W�F�N�g�̌^���K�v�ɂȂ������̒i�K�ŁA
 * �����o�ϐ��錾���ɐ錾���ꂽ�ʂ�̏����l�錾/�C���X�^���X�����s����B
 * �V���O���X���b�h�̃v���O�����łȂ�A���̕s�m��ȃ^�C�~���O�ł̏����l�錾/�C���X�^���X���́A
 * �傫�Ȗ��ɂ͂Ȃ�ɂ����ƍl������B
 * �������A����CosmicBarrage�̂悤�Ƀ}���`�X���b�h�ł̓����O��Ƃ���ƁA
 * 1�̕��@�ɂ��u�����I������/�C���X�^���X���v�͊댯�ł���B
 * ���̂Ȃ�A�����̃X���b�h�����̃N���X�̃����o�ɃA�N�Z�X���邱�Ƃɂ��A
 * �����o�ϐ��̔񓯎��I������/�C���X�^���X�����N����A�����̃����o�ϐ�/�I�u�W�F�N�g�Ԃł�
 * �������������郊�X�N�����邽�߂ł���B
 * 
 * ���̂��߁A�}���`�X���b�h�v���O���~���O���s���ꍇ��A
 * ���̕ϐ��̒l�������I�ɐ錾����Ă��Ȃ��ƒv���I�ȕs�s������������ꍇ�́A
 * �����o�̏�����/�C���X�^���X���́A�\�Ȍ��薾���I�ɃR���X�g���N�^���ōs���ׂ��ł���ƍl������B
 * 
 * �������ϐ��̏��������R���X�g���N�^�ɑS�Ď����Ă����ƁA
 * ����͂���ŃR���X�g���N�^����剻����Ƃ�����肪�o�Ă���B
 * ���Ƃ��Ƃ̃R���X�g���N�^���傫���̂ŁA�����o������/�C���X�^���X��������ꍇ�A
 * ���ꎩ�̂�ʃ��\�b�h�ɐ؂�o���Ȃǂ̎�Ԃ�����ꍇ���z��ł���B
 * �����o�ϐ��́u�����I������/�C���X�^���X���v�ɂ�茩�z�����s�s���ƁA
 * �R���X�g���N�^�̔�剻�ɂ��R�[�h�̉ǐ��̒ቺ�́A���V���ɂ������f���s�����ƁB
 * */
