package com.cosmicbarrage;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.cosmicbarrage.DrawEventManager.DrawingOrder;

/**
 * CosmicBarrage�̍ō������N���X�BJFrame���p�����Ă���B<br><br>
 * <b>���t�@�N�^�����O</b><br>
 * ����܂�CosmicBarrage�̃O���t�B�b�N�ɂ��āA<br>
 * �`����W�̊�͂��̃t���[�����g�ł͂Ȃ��A<br>
 * ���̃t���[���ɂ͂ߍ��܂ꂽCanvas�N���X�̃C���X�^���X�ł���AoutPutScreen�Ƃ���B<br>
 * ���ڐe�t���[���̍��W���Q�Ƃ���̂͊m���ɊȒP�����A<br>
 * ��͂�e�t���[���́u�g�v�����邱�Ƃɂ����W�̃Y���͖����ł���t�@�N�^�[�ł͂Ȃ��B<br>
 * ���AoutPutScreen�̓C���X�^���X���̎��_�ŁA�t���[���́u�g�v�ɂ��<br>
 * ���W�̃Y����␳������ŃZ�b�g����Ă���̂ŁA�����ɍ��W���w��ł���B
 *
 * @author Yuichi Watanabe
 * @since 2012/11/10
 * @version 1.10 Refactoring started on 2012/12/19
 * */
public class CosmicBarrage extends JFrame implements DrawEventListener{

	private static final long serialVersionUID = 1L;

	/* ###################
	 * �����o�ϐ�&�I�u�W�F�N�g�Q
	 * ###################*/

	static CosmicBarrage cosmicBarrage;
	Graphics g;

	//�{�[�����i�[�������X�g�B
	//�{�[���͂���ȍ~�̋@�\�ǉ��ŕ����𓯎��ɏo���\��Ȃ̂ŁA���X�g�ŉ^�p
	List<Ball> ballList;

	public Bar bar;
	public MyTaskPerFrame frameRenewer;
	public RaysAndParticles raysAndParticles;
	int XnewZero;
	int YnewZero;
	public Canvas outputScreen;
	EnemyHive enemyHive;//�@�\��F�X�ǉ������G�̃��X�g
	public Enemy enemy;

	//Ball��X��Y�̓�o�͂𐧌䂷�郍�b�J�[
	public Object XYLocker = new Object();

	//����̃_�u���o�b�t�@�����O�A���S���Y���Ɏg���C���[�W
	public Image displayingImage;
	public Graphics gForBuffer;

	//�e�C���X�^���X�ɑ΂��A��Ăɕ`��𑣂��C�x���g�𔭍s����C���X�^���X
	public DrawEventManager drawEventManager;

	//���݃Q�[�����ǂ̃t�F�C�Y�ɂ��邩�������񋓌^
	public GamePhase gamePhase = GamePhase.ON_INITIALIZING;

	//Enter�L�[�������ꂽ���ǂ������`�F�b�N����t���O
	private boolean isEnterPressed = false;

	//�_�ł���I�u�W�F�N�g�̖��ł��Ǘ�����ϐ�
	private int blinker = 0;

	//�Q�[���J�n���O�̃J�E���g�_�E�����Ǘ�����ϐ�
	private int timeCount = 3000;

	//���݃L�[��͂��󂯕t���邩�ǂ����������t���O�B
	//�Q�[���I�[�o�[�̉��o�Ȃǂɗ��p
	private boolean acceptsKeyInput = true;

	//���̃Q�[�����̊e��G�t�F�N�g�̕`����s���N���X
	EffectDrawer effectDrawer;

	//���݂̃X�R�A��o�ߎ��ԂȂǂ����j�^�����O����N���X
	GameMonitor gameMonitor;

	//�{�[���ɐU��ʂ�ID�B�{�[���͂��ꂼ��ɐU���Ă���ID���A
	//���̃Q�[���̃Z�b�V������ʂ��ĕK����ӂł���K�v������B
	//�i����������ƁA��x�g�����ԍ��͓�x�Ǝg���Ă͂Ȃ�Ȃ��j
	//GameMonitor�N���X�̃{�[�������A���S���Y�����H�v���āA���������
	int serialID = 0;

	/* ###################
	 * ���C�����\�b�h&�R���X�g���N�^
	 * ###################*/

	/*
	 * �ȉ��A�v���O�����J�n���̃X�^�[�e�B���O�ɋN�������A
	 * ��̃��\�b�h���L�q����
	 * */

	/**
	 * ���C�����\�b�h
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new CosmicBarrage();
	}

	/**
	 * �R���X�g���N�^
	 * */
	CosmicBarrage(){

		//�t���[���ݒ�
		this.setTitle("Cosmic Barrage!");
		this.setVisible(true);
		int XMod = this.getInsets().left + this.getInsets().right;
		int YMod = this.getInsets().top + this.getInsets().bottom;
		this.setBounds(100, 100, (640 + XMod), (480 + YMod));
		this.setResizable(false);//�t���[���T�C�Y�͌Œ�
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//�t���[���ɂ͂ߍ��ރL�����o�X��ݒ�B
		//�ȍ~�A���̃N���X�̃����o�ɂȂ��Ă���N���X�ŕ`�悪���ނ��̂́A
		//�S�Ă���outPutScreen�̍��W���Q�Ƃ��邱��
		outputScreen = new Canvas();
		outputScreen.setLocation(XMod, YMod);
		outputScreen.setBounds(0, 0, 640, 480);
		this.add(outputScreen);

		XnewZero = this.getInsets().left;
		YnewZero = this.getInsets().top;

		g = outputScreen.getGraphics();

		//�C���[�W����
		displayingImage = createImage(640, 480);
		gForBuffer = displayingImage.getGraphics();

		//�L�[���X�i�[�@�\��ǉ����A�Q�[���̃R���g���[�����\�Ƃ���
		this.addKeyListener(new myKeyListener());
		//�L�[���X�i�[�����������N���X���A�C���X�^���X�����ăt���[���ɑg�ݍ��ނ��A
		//�t���[���N���X���Ŗ����N���X�Ƃ��Đ錾����K�v������̂�Tips! ���ȁH

		/*DrawEventManager���C���X�^���X���B
		�Ȃ��A�����DrawEventListener�����������e��N���X����ɃC���X�^���X�����Ȃ��ƁA
		NullPointerException���N����̂Œ���*/
		drawEventManager = new DrawEventManager();

		//���g�̕`��C�x���g��drawEventManager�ɓo�^
		drawEventManager.addDrawEvent(this, DrawingOrder.LAST);

		//RaysAndParticles���C���X�^���X�����A���̃X���b�h���N���B
		//RaysAndParticles�͔w�i�����Ȃ̂ŁA���̃^�C�~���O�ŃC���X�^���X������
		raysAndParticles = new RaysAndParticles(this);
		raysAndParticles.flyerThread.start();

		//���̎��_�ŁA�ŏ��ɕ`�悵�����I�u�W�F�N�g�̃C���X�^���X���͍ς�ł���B
		//������frameRenewer���N�����A��ʂ̃t���[�����Ƃ̍X�V���J�n
		frameRenewer = new MyTaskPerFrame(g, this);
		frameRenewer.start();

		//EffectDrawer���C���X�^���X��
		effectDrawer = new EffectDrawer(this);

		//gameMonitor���C���X�^���X��
		gameMonitor = new GameMonitor(this);

		//�����frameRenewer�̕ʃX���b�h���N�����Ă���B
		//���̃R���X�g���N�^�����s���Ă���X���b�h�i���C���X���b�h�j�ł́A
		//�Ō��waitStarting���\�b�h���N�����āA�v���C���[�̎��̓�͂�҂�
		waitStarting();

	}//�R���X�g���N�^�I��

	/* ############################
	 * �C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h
	 * ############################*/

	/*
	 * �ȉ��A�C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h�A
	 * ����т��̃��\�b�h������Ăяo�����T�u���\�b�h��񋓂���B
	 * ���̃N���X�̕`��Œʒm�����̂́A��ɃQ�[�����e�S�̂Ɋւ���Ă���d�v�Ȓʒm�ł���*/

	/**CosmicBarrage�{�̂ɂ�����`��C�x���g�B
	 * ��Ƀ^�C�g���\����Q�[���I�[�o�[��ʂ̕\���Ȃǂ�S������B
	 * �����̃o���G�[�V�������L���Ȃ̂ŁA���݂̃Q�[���t�F�B�Y�Ȃǂɉ����āA
	 * ������if�߂ŕ��򂳂��A�X�ɂ���If�ߓ��ɋL�q�����T�u���\�b�h�ɏ�����؂�o���Ă���*/
	public void onDrawEvent(Image targetImage) {
		// TODO Auto-generated method stub

		//���݂̃t�F�C�Y���^�C�g����ʂɂ���ꍇ
		if (this.gamePhase == GamePhase.ON_TITLE) {
			displayTitle(targetImage);
		}
		//���݂̃t�F�C�Y���J�E���g�_�E���ɂ���ꍇ
		else if (this.gamePhase == GamePhase.ON_COUNT_DOWN) {
			displayCountDown(targetImage);
		}
		//���݂̃t�F�C�Y���Q�[���I�[�o�[�ɂ���ꍇ
		else if (this.gamePhase == GamePhase.ON_GAMEOVER) {
			displayGameOver(targetImage);
		}
		//���݂̃t�F�C�Y���Q�[���N���A�ɂ���ꍇ
		else if (this.gamePhase == GamePhase.ON_GAME_CLEARED) {
			displayGameClear(targetImage);
		}
	}

		/**�^�C�g���摜��\������T�u���\�b�h�B<br>
		 * �^�C�g���ɕK�v�ȉ摜�����\�[�X���烍�[�h���A�\������*/
		private void displayTitle(Image targetImage) {
			// TODO Auto-generated method stub
			Graphics g = targetImage.getGraphics();

			//�^�C�g���摜�ƃC���t�H���[�V�����摜�A�Ō��\�����擾
			BufferedImage title = null;
			BufferedImage information = null;
			BufferedImage logo = null;
			try {
				title
				= ImageIO.read(getClass().getResource
						("COSMIC_BARRAGE!.bmp"));
				information
				= ImageIO.read(getClass().getResource
						("Press_Enter_to_start.bmp"));
				logo
				= ImageIO.read(getClass().getResource
						("Logo.bmp"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//�摜�̊g�嗦��ݒ�
			int mag = 4;//mag="magnification"�̈�

			//�^�C�g���ƃC���t�H���[�V��������ʂɕ\���B
			//�C���t�H���[�V������blinker=1�̂Ƃ������\�����A�_�ŃG�t�F�N�g��������
			int X_image
			= (displayingImage.getWidth(outputScreen) - title.getWidth() * mag) / 2;
			g.drawImage(title,
						X_image, 160,
						title.getWidth() * mag, title.getHeight() * mag,
						cosmicBarrage);
			if (blinker == 1) {
				X_image
				= (displayingImage.getWidth(outputScreen) - information.getWidth() * mag) / 2;
				g.drawImage(information, X_image, 320,
							information.getWidth() * mag, information.getHeight() * mag,
							cosmicBarrage);
			}

			//���S�́A�g�嗦2�{�ŕ\��
			mag = 2;
			X_image
			= (displayingImage.getWidth(outputScreen) - logo.getWidth() * mag);
			int Y_image
			= (displayingImage.getHeight(outputScreen) - logo.getHeight() * mag);
			g.drawImage(logo, X_image, Y_image,
						logo.getWidth() * mag, logo.getHeight() * mag,
						cosmicBarrage);


		}//displayTitle�I��

		/**�J�E���g�_�E���摜��\������T�u���\�b�h�B<br>
		 * timeCount�����������ɂ���āA�\������摜��ς��Ă���*/
		private void displayCountDown(Image targetImage) {
			//�O���t�B�b�N�X���Z�b�g
			Graphics g = targetImage.getGraphics();

			//�\���摜�̌^�錾�B���݂�timeCount��0���傫���Ȃ�
			//�����̉摜�B�����łȂ��Ȃ�go�̉摜���Z�b�g
			BufferedImage myImage = null;


			//���݂�timeCount�̒l�ɉ����āA�J�n�܂ł̎c��b����\�������摜���擾�B
			//���̐����摜�̃����_�����O�ɂ́ADigitManager�N���X�̃C���X�^���X���g�p�B
			//�����ɁA���o�̂��߂ɁA�摜�̊g�嗦���b���ɉ����ĎZ�o
			DigitManager digitManager = new DigitManager();
			//�摜�̊g�嗦��ݒ�
			int mag = 8;//mag="magnification"�̈�
			//�c��3�b
			if (timeCount > 2000) {
				myImage = digitManager.getDigitGraphic(3);
				mag = 8;
			}
			//�c��2�b
			else if (timeCount > 1000) {
				myImage = digitManager.getDigitGraphic(2);
				mag = 12;
			}
			//�c��1�b
			else if (timeCount >= 0) {
				myImage = digitManager.getDigitGraphic(1);
				mag = 16;
			}
			//�Q�[���J�n��
			else {
				try {
					myImage = ImageIO.read(getClass().getResource("GO!.bmp"));
					mag = 14;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}



			//�摜��\�����ׂ����W�̌v�Z
			int X_image
			= (displayingImage.getWidth(cosmicBarrage) - myImage.getWidth() * mag) / 2;
			int Y_image
			= (displayingImage.getHeight(cosmicBarrage) - myImage.getHeight() * mag) / 2;


			//�������̎��_��timeCount�������Ȃ�A�\������Ă���"GO!"�̕������A���X�ɏ�Ɏ����グ�Ă����A
			//-1000�ɒB�������_�Ŋ��S�ɉ�ʏォ����ł���悤�A���W�𒲐�����
			if (timeCount < 0) {
				Y_image = Y_image + (Y_image + myImage.getHeight() * mag) * timeCount / 1000;
			}

			//�摜�̕\��
			g.drawImage(myImage,
						X_image, Y_image,
						myImage.getWidth() * mag, myImage.getHeight() * mag,
						cosmicBarrage);

			//�������̎��_��timeCount��-1000�ȏ�Ȃ�A
			//�Q�[���t�F�C�Y��ύX��timeCount��3000�Ƀ��Z�b�g(����ȍ~�̃Q�[���ɔ�����)
			if ( timeCount <= (-1000) ) {
				timeCount = 3000;
				gamePhase = GamePhase.ON_GAME;
			}

		}//displayCountDown�I��

		/**�Q�[���I�[�o�[�摜��\������T�u���\�b�h�B<br>
		 * displayTitle���\�b�h�𗬗p���ABlinker���Q�Ƃ���*/
		private void displayGameOver(Image targetImage) {
			//Graphics���Q�b�g
			Graphics g = targetImage.getGraphics();

			//�Q�[���I�[�o�[�摜�ƃC���t�H���[�V�����摜���擾
			BufferedImage gameover = null;
			BufferedImage information = null;
			try {
				gameover = ImageIO.read(
						getClass().getResource("GAME_OVER.bmp"));
				information = ImageIO.read(
						getClass().getResource("Press_Enter_and_return_to_title.bmp"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//�摜�̊g�嗦��ݒ�
			int mag = 4;//mag="magnification"�̈�

			//�^�C�g���ƃC���t�H���[�V��������ʂɕ\���B
			//�C���t�H���[�V������blinker=1�̂Ƃ������\�����A�_�ŃG�t�F�N�g��������
			int X_image
			= (displayingImage.getWidth(cosmicBarrage) - gameover.getWidth() * mag) / 2;
			g.drawImage(gameover,
						X_image, 160,
						gameover.getWidth() * mag, gameover.getHeight() * mag,
						cosmicBarrage);
			if (blinker == 1) {
				X_image
				= (displayingImage.getWidth(cosmicBarrage) - information.getWidth() * mag) / 2;
				g.drawImage(information, X_image, 320,
							information.getWidth() * mag, information.getHeight() * mag,
							cosmicBarrage);
			}

		}

		/**�Q�[���N���A�摜��\�����郁�\�b�h�B<br>
		 * displayTitle���\�b�h�𗬗p���ABlinker���Q�Ƃ���*/
		private void displayGameClear(Image targetImage) {
			// TODO Auto-generated method stub
			//Graphics���Q�b�g
				Graphics g = targetImage.getGraphics();

			//�Q�[���N���A�摜�ƃC���t�H���[�V�����摜���擾
			BufferedImage gameCleared = null;
			BufferedImage information = null;
			try {
				gameCleared = ImageIO.read(
						getClass().getResource("GAME_CLEARED.bmp"));
				information = ImageIO.read(
						getClass().getResource("Press_Enter_and_try_new_game.bmp"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//�摜�̊g�嗦��ݒ�
			int mag = 4;//mag="magnification"�̈�

			//�N���A���b�Z�[�W�ƃC���t�H���[�V��������ʂɕ\���B
			//�C���t�H���[�V������blinker=1�̂Ƃ������\�����A�_�ŃG�t�F�N�g��������
			int X_image
			= (displayingImage.getWidth(cosmicBarrage) - gameCleared.getWidth() * mag) / 2;
			g.drawImage(gameCleared,
						X_image, 160,
						gameCleared.getWidth() * mag, gameCleared.getHeight() * mag,
						cosmicBarrage);
			if (blinker == 1) {
				X_image
				= (displayingImage.getWidth(cosmicBarrage) - information.getWidth() * mag) / 2;
				g.drawImage(information, X_image, 320,
							information.getWidth() * mag, information.getHeight() * mag,
							cosmicBarrage);
			}

		}//displayGameClear���\�b�h�I��

	/* ###############
	 * �Q�[���i�s�p���\�b�h
	 * ###############*/

	/*
	 * �ȉ��A�Q�[���̃t�F�C�Y��i�s������ۂɎg���郁�\�b�h�Q�B
	 * Enter�L�[�̓�͂��󂯕t������A��ʂ̃J�E���g�_�E�����n�߂���Ȃǂ́A
	 * �Q�[���J�n�܂ł̃V�[�N�G���X��K���ȗ��x�ɐ؂蕪�������\�b�h��u�����B
	 * �ȉ��̃��\�b�h�̓��\�b�h����ABC���ł͂Ȃ��A���s����鏇�ɋL�q���Ă���
	 * */

	/**
	 * Phase1:�^�C�g���\���p���\�b�h�B<br>
	 * gamePhase�t���O���Z�b�g�����Ȃ�AEnter�L�[�̓�͂�����܂Ń��[�v���đҋ@����
	 * */
	void waitStarting(){

		//��������AEnter�L�[��͂����Z�b�g
		isEnterPressed = false;

		//�Q�[���t�F�C�Y��onTitle�ɕύX�B����ɂ��onDrawEvent��displayTitle���\�b�h��
		//�N������悤�ɂȂ�
		gamePhase = GamePhase.ON_TITLE;

		//displayTitle���\�b�h�ɂ����āA�^�C�g���_�ł̃G�t�F�N�g�������邽�߁A
		//�ʃX���b�h�𔭍s�B
		//�^�C�g���_�ł��s���ɂ͂������ϐ���p�ӂ���K�v������̂ŁA
		//�����̕ϐ������b�v���ĉ^�p����ړI�ƁA
		//���\�b�h�̉ǐ�����コ����ړI�����˂āA�����N���X�ɐ؂�o��
		BlinkerThread blinkerThread = new BlinkerThread();

		//�X���b�h�����s
		blinkerThread.start();

		//�����āA���̃X���b�h���̃��[�v���I���̂�ҋ@
		try {
			blinkerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//�����āA��ʂ͎��̒i�K��prepareObjects���\�b�h�ֈڂ�
		prepareObjects();
	}

	/**
	 * Phase2:�Q�[����̃I�u�W�F�N�g�̏������\�b�h�B<br>
	 * �^�C�g����ʂł�Enter��͂��󂯂āA�e��I�u�W�F�N�g���C���X�^���X�����A<br>
	 * �Q�[���J�n�ɔ�����B<br>
	 * �Ȃ��A�o�[�A�{�[���A�G�̏��ԂŃC���X�^���X���͍s���A<br>
	 * �G���S�ĉ�ʂɕ\������I�������_�ŁA�G�̓�����i��X���b�h�������N���B<br>
	 * (�{�[���ƃo�[�̃X���b�h�͂��̃^�C�~���O�ł͋N�����Ȃ�)<br>
	 * �����܂ŏ������I������Ȃ�A����startCountDown���\�b�h�Ɉڍs
	 * */
	void prepareObjects(){
		//�Q�[���t�F�C�Y��ύX���A��ʕ\�������ł�����
		gamePhase = GamePhase.OTHER;

		//���o�̂��߁A500ms�����X���[�v
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//Bar���C���X�^���X��(Ball�N���X��Bar�N���X���Q�Ƃ���̂ŁA��ɃC���X�^���X���̕K�v����)
		bar = new Bar(this);

		//ballList���C���X�^���X�����A���̒���1�����{�[����ǉ�
		serialID = 0;
		ballList = new ArrayList<Ball>();
		ballList.add(new Ball(this, serialID));
		serialID = serialID + 1;

		//enemyHive���C���X�^���X��
		enemyHive = new EnemyHive(this);

		int enemyID = 0;

		//����enemyList�̒��ɁA�v140�̂̓G��˂�����ł݂�B
		//��s�Ԃ��20�́A���ꂪ7��ł��̊���B
		//�Ȃ��A�G��x�Ay���W�̒����w��̊ϓ_����Afor�߂̓�d�l�X�g���̗p�B
		//�Q�[����̉��o�̂��߁A1�̃C���X�^���X�����s�����ƂɁA1�t���[�������X���[�v
		for (int i = 0; i < 7; i ++) {

			for(int j = 0; j < 20; j ++){
				//new���g���Ă���̂ŁAEnemy�̃R���X�g���N�^�͖��Ȃ��N�����Ă���
				enemyHive.add(new Enemy(this, j * 32, i * 32, (7 - i), enemyID));
				enemyID = enemyID + 1;

				//���o�̂��߂�0.5�t���[���X���[�v
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//����for�ߏI��

		}//�O��for�ߏI��

		//�G���S���o�������Ƃ���ŁAEnemyHive�̓����X���b�h���N�����āA
		//�G�̃A�j���[�V�������J�n
		enemyHive.imageIndexChanger.start();

		//startCountDown���\�b�h�̏����Ɉڍs
		startCountDown();

	}//prepareObjects���\�b�h�I��

	/**
	 * Phase3:�Q�[���J�n�̃J�E���g�_�E���p���\�b�h�B<br>
	 * �J�E���g�_�E�����J�n���AtimeCount��0�ɂȂ�܂�main�X���b�h��ҋ@�����郁�\�b�h�B<br>
	 * timeCount�́A���̃N���X���Ŕ��s����閳���X���b�h�ɂ���Č����Ă����B<br>
	 * �ҋ@���I������u�ԂɃ{�[���ƃo�[�̃X���b�h���N�������A���悢��Q�[���{�҂��X�^�[�g
	 * */
	private void startCountDown(){
		//�Q�[���t�F�C�Y��ON_COUNT_DOWN�ɏ�������
		this.gamePhase = GamePhase.ON_COUNT_DOWN;

		//timeCount�����炵�Ă䂭�����X���b�h�𔭍s���A���̏�Ŏ��s
		Thread countReducingThread = new Thread(){
			@Override
			public void run(){
				//�Q�[���̉��o�̓s����A������-1000�܂ŃJ�E���g�B
				while(timeCount >= (-1000)){
					//���ݕ���-1�ł͂Ȃ�-2�B-1���ƃJ�E���g�_�E�����x���Ă������肷��
					timeCount = timeCount - 2;
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//���[�v�I��
			}//run���\�b�h�I��
		};//�����N���X�I��

		countReducingThread.start();

		//�Q�[���t�F�C�Y�̏��������ɂ��AdisplayCountDown���\�b�h���N���B
		//disPlayCountDown���\�b�h�ŕϐ�timeCount�������Ă����̂ŁA
		//timeCount��0�ɂȂ�܂őҋ@
		while(timeCount >= 0){
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//�^�C�}�[���N��
		gameMonitor.startTimer();

		//���݊i�[����Ă���{�[����S�ē�����
		for (int i = 0; i < ballList.size(); i++ ){
			ballList.get(i).myTaskPerFrame.start();
		}

		//�o�[�̈ړ����\�ɂ���
		bar.barMovingThread.start();

		//�����āAgamePhase��OnGame�Ƀ`�F���W
		gamePhase = GamePhase.ON_GAME;

	}

	/**
	 * Phase4:�Q�[���I�[�o�[�̃`�F�b�N���\�b�h�B<br>
	 * �Q�[���̏�Ԃ��Q�[���I�[�o�[�ɂȂ��Ă��Ȃ����ǂ������`�F�b�N���郁�\�b�h�B<br>
	 * ��̓I�ɂ́A�ȉ���2�����̂ǂ��炩���������ꂽ�ꍇ�A�Q�[���I�[�o�[�Ƃ݂Ȃ��B<br><br>
	 * <b>1.ballList�ɓo�^����Ă���{�[���̐���0�ɂȂ����Ƃ�</b><br>
	 * <b>2.ballList�ɓo�^����Ă���S�Ẵ{�[����isBroken�t���O��true�ɂȂ����Ƃ�</b><br><br>
	 * �Q�[���I�[�o�[�����𖞂������ꍇ�A<br>
	 * �����ɃL�[��͂����b�N���AgamePhase��ON_GAMEOVER�ɏ��������A<br>
	 * �ʃX���b�h�𔭍s���ăQ�[���I�[�o�[�������s���B<br>
	 * (���̃��\�b�h�͒ʏ�AMytaskPerFrame����N�������̂ŁA�ʃX���b�h�𔭍s���Ȃ���<br>
	 * ��ʕ`��̏������~�܂��Ă��܂�)<br>
	 * */
	private void checkGameOver() {
		// TODO Auto-generated method stub

		/* ########################
		 * �Q�[���I�[�o�[�t���O�`�F�b�N�̕�
		 * #######################*/

		//�܂��́AballList���X�L�������āA�Q�[���I�[�o�[�t���O�̏����B
		//�t���O�̏���l��true�ɐݒ�
		boolean isGameOver = true;

		//�{�[���̐���0���傫���Ȃ�Q�[���I�[�o�[�t���O���܂��
		if (ballList.size() > 0) {
			isGameOver = false;
		}

		//isBroken�t���O��false�A����isBeingGenerated�t���O��false�ł���
		//�{�[������ȏ゠��Ȃ�A�Q�[���I�[�o�[�t���O���܂��
		for (int i = 0; i < ballList.size(); i++) {
			if ( (ballList.get(i).isBroken == false) &&
					(ballList.get(i).isBeingGenerated == false) ) {
				isGameOver = false;
				break;//��ł��{�[���������Ă���΂���ȏ�͊m�F�s�v
			}
		}

		//�܂��A���̔����A����ballList�Ɋi�[����Ă���{�[���ɂ��āA
		//�S�Ă�isBroken=true����isBeingGenerated=false�ł���
		//���j���Ă��Ȃ��S�Ẵ{�[���ɂ��āA�S�Ă����U�A�j���[�V������
		//�`�撆�Ȃ�A���o�̂��߂ɃL�[��͂𓀌�
		boolean willKeyBeLocked = true;

		for(int i = 0; i < ballList.size(); i++) {
			if ( (ballList.get(i).isBroken == true) && (ballList.get(i).isBeingGenerated == false) ) {
				//Do nothing
			}else{
				willKeyBeLocked = false;
				break;//�t���O����x�܂ꂽ�炻��ȏ�̃��[�v�͕s�v
			}
		}

		if ( (willKeyBeLocked == true)  ) {
			//acceptsKeyInput��false�ɂ��A�L�[����𓀌��B
			//������bar�̓�͏�Ԃ��j���[�g�����ɋ����ύX
			acceptsKeyInput = false;
			bar.isKeyPressed = false;
			bar.direction = "Neutral";
		}

		/* ######################
		 * �Q�[���I�[�o�[�������s�̕�
		 * ######################*/

		//���̎��_�ŃQ�[���I�[�o�[�t���O�������Ă���Ȃ�A�Q�[���I�[�o�[����
		if ( isGameOver == true ) {

			//�^�C�}�[���~
			gameMonitor.timer.cancel();

			//�����ł�������Q�[���t�F�C�Y��OTHER�ɕς��A
			//�t���[���X�V���Ƃɉ��̖����N���X�̃X���b�h���������s����Ă��܂�
			//�o�O��h���B����𐳎���GAMEOVER�ɍ��킹��̂́A
			//���̃X���b�h�̒��ōs��
			gamePhase =GamePhase.OTHER;

			//�����N���X�ŕʃX���b�h�𔭍s���A�Q�[���I�[�o�[�����������ōs���B
			//�Ȃ����̃X���b�h�͈�������s����悤�ɂ��Ȃ��ƁA
			//��ʍX�V�̂��тɂ���checkGameOver���\�b�h���N������A
			//���X�ȉ��̃X���b�h�����ꑱ���ăo�O���O���R�ς݂ɂȂ�̂Œ���
			Thread gameOverThread = new Thread(){
				@Override
				public void run() {
					processGameOver();
				}
			};

			//�Q�[���I�[�o�[�X���b�h���X�^�[�g
			gameOverThread.start();

		}//if�ߏI��

	}//checkGameOver���\�b�h�I��

		/**
		 * �Q�[���I�[�o�[���̎����I�����B
		 * �{�[���̔��U�A�j���[�V�������I���܂őҋ@������A
		 * �c�����G�ɂ��āA��񂸂Еt���Ă���
		 * */
		private void processGameOver() {

			//�Q�[���I�[�o�[�������n�߂�O�ɁA�S�Ẵ{�[���̔��U�A�j���[�V�������I���
			//=ballList��size��0�ɂȂ�܂ŏ�����ҋ@
			while (this.ballList.size() > 0) {
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//�ҋ@���I������Ȃ�A�܂��͖��j��̓G��j��B
			//���̎��_�ł́AenemyList�̒������擾�B
			//enemyList��size�́A�ȉ���for�߂̏����Ń��A���^�C���ω����N����̂ŁA
			//���̎��_�̒l��ۑ����Ȃ���΂����Ȃ�
			int enemyNumber = enemyHive.size();

			//�G���N���A����ۂ́Aenemy�̃f�X�g���N�^���g���B
			//�Ȃ�enemyList��List�Ȃ̂ŁA�v�f�̍폜���ƂɎ����I�Ɍ���
			//�v�f���O�ɋl�߂Ă����Bfor�߂Œ����폜���s���ꍇ�A
			//�C���f�b�N�X��i�܂���K�v�͂Ȃ��̂Œ���
			for (int i = 0; i < enemyNumber; i++) {
				enemyHive.get(0).destructEnemy();

				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//0.5�t���[�������X���[�v

			}

			//������bar��j��
			bar.destructBar();

			//�X�ɂ��̃^�C�~���O�ŁATitle�ł��g����BlinkerThread��
			//�ĂуC���X�^���X�������Ďg�p
			BlinkerThread blinkerThread = new BlinkerThread();
			blinkerThread.start();

			//�Q�[���t�F�C�Y���Q�[���I�[�o�[�ɃZ�b�g�B
			//����ɂ��A�Q�[���I�[�o�[��ʂ̕\�����g���K�[
			gamePhase = GamePhase.ON_GAMEOVER;

			//�����������܂ŗ�����A�L�[�C���v�b�g�̃��b�N������
			acceptsKeyInput = true;

			//���Ƃ́AblinkerThread�ɂ��摜�_�ł��AEnter�L�[��͂ŏI������̂�ҋ@
			try {
				blinkerThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//try�ߏI��

			//������blinkerThread���I��������A�ŏ��̃��\�b�h��������x�N�����A
			//�^�C�g����ʂɖ߂�B���̍�timeCount�����Z�b�g���A�^�C�}�[���j��
			timeCount = 3000;
			gameMonitor = null;
			gameMonitor = new GameMonitor(this);
			waitStarting();
		}

	/**
	 * Phase5:�Q�[���N���A�̃`�F�b�N���\�b�h�B<br>
	 * �Q�[���̏�Ԃ��Q�[���N���A�ɂȂ��Ă��邩�ǂ������`�F�b�N���郁�\�b�h�B<br>
	 * ��̓I�ɂ́A����2�����̂ǂ��炩���������ꂽ�ꍇ�A�Q�[���N���A�Ƃ݂Ȃ��B<br><br>
	 * <b>1.EnemyList�ɓo�^����Ă���G�̐���0�ɂȂ����Ƃ�</b><br>
	 * <b>2.EmemyList�ɓo�^����Ă���S�Ă̓G�̂�isAlive�t���O��false�ɂȂ����Ƃ�</b><br><br>
	 * �Q�[���N���A�����𖞂������ꍇ�A<br>
	 * �����ɃL�[��͂����b�N���AgamePhase��ON_GAME_CLEAR�ɏ��������A<br>
	 * �ʃX���b�h�𔭍s���ăQ�[���N���A�������s���B<br>
	 * (���̃��\�b�h�͒ʏ�AMytaskPerFrame����N�������̂ŁA�ʃX���b�h�𔭍s���Ȃ���<br>
	 * ��ʕ`��̏������~�܂��Ă��܂�)<br><br>
	 * �Ȃ��A�R�[�h��checkGameOver���\�b�h�̂��̂𗬗p
	 * */
	private void checkGameClear() {
		// TODO Auto-generated method stub

		/* #####################
		 * �Q�[���N���A�t���O�`�F�b�N�̕�
		 * #####################*/

		//�܂��́AEnemyList���X�L�������āA�Q�[���N���A�t���O�̏����B
		//�t���O�̏���l��true�ɐݒ�
		boolean isGameCleared = true;

		//�G�̐���0�Ȃ�Q�[���N���A�t���O������
		if (enemyHive.size() == 0) {
			isGameCleared = true;
		}

		//isAlive�t���O����ł������Ă���΁A�Q�[���N���A�t���O�͐܂��
		for (int i = 0; i < enemyHive.size(); i++) {
			if (enemyHive.get(i).isAlive  == true) {
				isGameCleared = false;
				break;
			}
		}

		/* ###################
		 * �Q�[���N���A�������s�̕�
		 * ###################*/

		//���̎��_�ŃQ�[���N���A�t���O�������Ă���Ȃ�A�Q�[���N���A����
		if ( isGameCleared == true ) {
			//�^�C�}�[���~
			gameMonitor.timer.cancel();

			//acceptsKeyInput��false�ɂ��A�L�[����𓀌��B
			//������bar�̓�͏�Ԃ��j���[�g�����ɋ����ύX
			acceptsKeyInput = false;
			bar.isKeyPressed = false;
			bar.direction = "Neutral";

			//�����ł�������Q�[���t�F�C�Y��OTHER�ɕς��A
			//�t���[���X�V���Ƃɉ��̖����N���X�̃X���b�h���������s����Ă��܂�
			//�o�O��h���B����𐳎���GAME_CLEARED�ɍ��킹��̂́A
			//���̃X���b�h�̒��ōs��
			gamePhase =GamePhase.OTHER;

			//�����N���X�ŕʃX���b�h�𔭍s���A�Q�[���N���A�����������ōs���B
			//�Ȃ����̃X���b�h�͈�������s����悤�ɂ��Ȃ��ƁA
			//��ʍX�V�̂��тɂ���checkGameCleared���\�b�h���N������A
			//���X�ȉ��̃X���b�h�����ꑱ���ăo�O���O���R�ς݂ɂȂ�̂Œ���
			Thread gameClearedThread = new Thread(){
				@Override
				public void run() {
					processGameCleared();
				}
			};

			//�Q�[���N���A�X���b�h���X�^�[�g
			gameClearedThread.start();

		}//if�ߏI��

	}//checkGameClear���\�b�h�I��

		/**
		 * �Q�[���N���A���̎����I�����B
		 * �G�̔��U�A�j���[�V�������I���܂őҋ@������A
		 * �c�����{�[����Еt���Ă���
		 * */
		private void processGameCleared() {
			// TODO Auto-generated method stub

			//�Q�[���N���A�������n�߂�O�ɁA�S�Ă̓G�̔��U�A�j���[�V�������I���
			//=enemyHive��size��0�ɂȂ�܂ŏ�����ҋ@
			while (this.enemyHive.size() > 0) {
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//�ҋ@���I������Ȃ�A�܂��̓{�[����j��B
			//���̎��_�ł́AenemyList�̒������擾�B
			//enemyList��size�́A�ȉ���for�߂̏����Ń��A���^�C���ω����N����̂ŁA
			//���̎��_�̒l��ۑ����Ȃ���΂����Ȃ�
			int ballNumber = ballList.size();

			//�{�[�����폜����ۂ́Aball�̃f�X�g���N�^���g���B
			//�Ȃ�ballList��List�Ȃ̂ŁA�v�f�̍폜���ƂɎ����I�Ɍ���
			//�v�f���O�ɋl�߂Ă����Bfor�߂Œ����폜���s���ꍇ�A
			//�C���f�b�N�X��i�܂���K�v�͂Ȃ��̂Œ���
			for (int i = 0; i < ballNumber; i++) {
				ballList.get(0).destructBall();

				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//0.5�t���[�������X���[�v

			}

			//������bar��j��
			bar.destructBar();

			//�X�ɂ��̃^�C�~���O�ŁATitle�ł��g����BlinkerThread��
			//�ĂуC���X�^���X�������Ďg�p
			BlinkerThread blinkerThread = new BlinkerThread();
			blinkerThread.start();

			//�Q�[���t�F�C�Y���Q�[���I�[�o�[�ɃZ�b�g�B
			//����ɂ��A�Q�[���I�[�o�[��ʂ̕\�����g���K�[
			gamePhase = GamePhase.ON_GAME_CLEARED;

			//�����������܂ŗ�����A�L�[�C���v�b�g�̃��b�N������
			acceptsKeyInput = true;

			//���Ƃ́AblinkerThread�ɂ��摜�_�ł��AEnter�L�[��͂ŏI������̂�ҋ@
			try {
				blinkerThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//try�ߏI��

			//������blinkerThread���I��������A�ŏ��̃��\�b�h��������x�N�����A
			//�^�C�g����ʂɖ߂�B���̍�timeCount�����Z�b�g���A�^�C�}�[���j��
			timeCount = 3000;
			gameMonitor = null;
			gameMonitor = new GameMonitor(this);
			waitStarting();
		}

	/* ########
	 * �����N���X
	 * ########*/

	/**��ʂɕ\�������C���[�W���A�����œ_�ł����邽�߂̃X���b�h���������N���X�B<br>
	 * ������́A���̃N���X�̃����o�ϐ�blinker���A������0��1�ɕω�������N���X�ŁA<br>
	 * ���̃N���X�ɂ�葀�삳���blinker���t���O�Ƃ��ĎQ�Ƃ��邱�ƂŁA<br>
	 * �C���[�W�̓_�ł��N�������Ƃ��ł���B*/
	class BlinkerThread extends Thread{

		int blinkingSpan = 500;//�_�Ŏ��
		int elapsedTime = 0;//�o�ߎ���(�~���b)

		//Enter�L�[��������A���̃��\�b�h�ɐi�܂��邩�ǂ����̃t���O
		boolean canProgress = false;

		//���[�v�𑱂���c�莞�ԁBcanProgress��false�ł������͌���Ȃ�
		int restRunningTime = 1500;

		@Override
		public void run(){

			while(restRunningTime >= 0){

				//����Enter�L�[��������Ă���Ȃ�AcanProgress�t���O��true�ɂȂ�B
				//������elapsedTime��1/2�ɂȂ�A"Press Enter to start"�̕�����
				//2000ms=2�b�ԒZ���̓_�ł��n�߂�
				if (isEnterPressed == true) {
					canProgress = true;
					blinkingSpan = blinkingSpan / 2;
				}

				//�o�ߎ��ԍ��v���_�Ŏ����z�����u�ԁAblinker�̔��]���s��
				if (elapsedTime >= blinkingSpan){

					//�o�ߎ��Ԃ����Z�b�g
					elapsedTime = 0;
					//blinker�̔��]
					if (blinker == 0){
						blinker = 1;
					}else{
						blinker = 0;
					}
				}

				//1�t���[���Ԃ�X���[�v�B�X���[�v�������ɂ̂݁A�e�펞�Ԋ֘A�̕ϐ����ω�����
				try {
					Thread.sleep(16);//�X���[�v����1�t���[���Ƃ��AEnter�L�[��͂��󂯕t����
					elapsedTime = elapsedTime + 16;//�o�ߎ��Ԃ�ǉ�

					//canProgress��true�Ȃ�΁A�c�莞�Ԃ���������
					if (canProgress == true) {
						restRunningTime = restRunningTime - 16;
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//���[�v�I��
		}//run���\�b�h�I��
	}

	/**
	 * �ȉ��ABar�̓����𐧌䂷�邽�߂̃��X�i�[�N���X�B�g���̂�keyPressed��keyReleased�����ł悢
	 * */
	public class myKeyListener implements KeyListener{
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
		}

		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

			//acceptsKeyInput��false�Ȃ狭�����^�[���ő���𖳌�
			if (acceptsKeyInput == false) {
				return;
			}

			//bar��null�łȂ��Ȃ�Abar�̕ϐ��𑀍�
			if ( (e.getKeyCode() == KeyEvent.VK_RIGHT) && (bar != null) ) {
				bar.isKeyPressed = true;
				bar.direction = "Right";
			}
			if ( (e.getKeyCode() == KeyEvent.VK_LEFT) && (bar != null) ) {
				bar.isKeyPressed = true;
				bar.direction = "Left";
			}
			//�G���^�[�L�[�������ꂽ�ꍇ
			if (e.getKeyCode() == KeyEvent.VK_ENTER){
				isEnterPressed = true;
			}
		}

		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

			//acceptsKeyInput��false�Ȃ狭�����^�[���ő���𖳌�
			if (acceptsKeyInput == false) {
				return;
			}

			//bar��null�łȂ��Ȃ�Abar�̕ϐ��𑀍�
			if ( (e.getKeyCode() == KeyEvent.VK_RIGHT) && (bar != null) ){
				bar.isKeyPressed = false;
				bar.direction = "Neutral";
			}
			if ( (e.getKeyCode() == KeyEvent.VK_LEFT) && (bar != null) ){
				bar.isKeyPressed = false;
				bar.direction = "Neutral";
			}
			//�G���^�[�L�[�������ꂽ�ꍇ
			if (e.getKeyCode() == KeyEvent.VK_ENTER){
				isEnterPressed = false;
			}
		}
	}

	/**
	 * Cosmic Barrage!�N���X�̓����N���X�B�t���[�����ƂɕK�v�ȏ������s���B
	 * ��ȏ����͖��t���[�����Ƃ̃Q�[����Ԃ̃`�F�b�N�A��ʕ`��ȂǁB
	 * ��ʂ̕`���@�͈ȉ��̒ʂ�B
	 * ���̃N���X��run���\�b�h���ADrawEventManager�ɓo�^���ꂽ
	 * �e�I�u�W�F�N�g�̕`��C�x���g���Ăяo���A�o�b�t�@�����O�p�C���[�W�ɕ`��B
	 * run���\�b�h�̍Ō�ɁA�o���オ�����o�b�t�@�����O�p�C���[�W��outputScreen�ɓ͂���A�Ƃ����d�l�Ƃ���
	 * */
	public class MyTaskPerFrame extends Thread{

		Graphics g;
		CosmicBarrage parentFrame;

		//�o�b�t�@�����O�C���[�W�p�̃C���[�W��Graphics�̌^
		Image imgBuffer;
		Graphics gForBuffer;

		MyTaskPerFrame(Graphics gInput, CosmicBarrage parentFrameInput){
			parentFrame = parentFrameInput;
			g = gInput;//outPutScreen��Graphics����

		}

		@Override
		public void run(){

			//imgBuffer������
			imgBuffer = createImage(640, 480);
			gForBuffer = imgBuffer.getGraphics();

			//�ȉ��̏����𖳌����[�v
			while(true){

				/* ##################################
				 * �Q�[����Ԃ̃`�F�b�N(�Q�[���{�Ԓ��̂ݎ��s)
				 * ##################################*/

				if (gamePhase == GamePhase.ON_GAME) {
					//�Q�[���I�[�o�[�����𖞂����Ă��܂��Ă��邩�`�F�b�N
					checkGameOver();
				}
				//���if�߂ŃQ�[���I�[�o�[�t���O�����\��������̂ŁA
				//�Q�[���N���A�t���O�̃`�F�b�N�͕ʂ�if�߂ɐ؂蕪����
				if (gamePhase == GamePhase.ON_GAME) {
					//�Q�[���N���A�����𖞂����Ă��邩���`�F�b�N
					checkGameClear();
				}

				/* ###########
				 * ��ʕ`��֌W
				 * ###########*/

				//�\�߃o�b�t�@�����O�C���[�W�𔒂œh��ׂ�
				this.gForBuffer.setColor(Color.WHITE);
				this.gForBuffer.fillRect(0, 0, 640, 480);

				//drawEvent���g���K�[���A���̃N���X���̉摜�����S�ẴC���X�^���X�ɁA
				//�`�施�߂𔭍s�B���̃t���[���ŕ\�����ׂ��摜�̍������ʂ��擾
				drawEventManager.fireDrawEvent(imgBuffer);

				//�Ō�ɁA�������ʂ̃o�b�t�@�C���[�W��outputScreen�ɓ��e
				this.g.drawImage(imgBuffer, 0, 0, parentFrame.outputScreen);

				try {
					sleep(16);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//try�ߏI��

			}//�������[�v�I��

		}//run���\�b�h�I��

	}//MyTaskPerFrame�N���X�I��

}//CosimicBarrage�N���X�I��

/**
 * ���݁A�Q�[�����ǂ̃t�F�C�Y�ɂ��邩�������񋓌^�B<br>
 * ���̗񋓌^���t���O�ɂ��āA�Q�[���i�s�ɂ܂�鏈�����s��
 * */
enum GamePhase{
	/**�Q�[�����e�̏���*/
	ON_INITIALIZING,
	/**�Q�[���^�C�g����\����*/
	ON_TITLE,
	/**�Q�[���J�n�̃J�E���g�_�E����*/
	ON_COUNT_DOWN,
	/**�Q�[�����s��*/
	ON_GAME,
	/**�Q�[���I�[�o�[*/
	ON_GAMEOVER,
	/**�Q�[���N���A*/
	ON_GAME_CLEARED,
	/**����ȊO�̏��*/
	OTHER
}


/**
 * Lock�ɂ��Ă�Tips�B
 * Java�ł�synchronized�X�e�[�g�����g���g�����ƂŁA���̋�Ԃ�r���I�Ɏ��s�ł���Ƃ����e�N�j�b�N������B
 * ���̍�synchronized�X�e�[�g�����g�̈�Ƃ��ăI�u�W�F�N�g��n��(��:public Object obj�Ȃ�)���A
 * ���̍ۓn�����obj�́A�uobj�̃X�R�[�v���ɂ��邠�������̃N���X�̃��\�b�h�����s���Ă���X���b�h���A
 * ���݂��ɓ�����������{�[���v�ƍl���Ă�������������Ȃ��B
 * ����u�Ԃɂ����āA���́u�{�[���v�������Ă���X���b�h�́A�����̎��s���Ă��郁�\�b�h���́A
 * synchronized�X�e�[�g�����g���̃R�[�h�����s����A���s�������B
 * ������synchronized�X�e�[�g�����g���̏������I������A���̃X���b�h�́u�{�[���v�����ē����グ��B
 * ���́u�����グ�v��ꂽ�u�{�[���v�ɁA����synchronized�X�e�[�g�����g�������\�b�h�����s���Ă���X���b�h����т��A
 * �V���ɂ��́u�{�[���v�����񂾃X���b�h�́A���g�����s���Ă��郁�\�b�h���́Asynchronized�X�e�[�g�����g�Ɉ͂܂ꂽ
 * �����̃R�[�h�����s���錠������ɓ���B
 * ���Ƃ͂�����J��Ԃ��āA�S�Ă�synchronized�X�e�[�g�����g�������\�b�h�����s���̃X���b�h���I������܂ŁA
 * �uLockObject�v�Ƃ������́u�{�[���v�̓��������́A�����X���b�h�Ԃő����Ă����c�c�B
 * ����ȃA�i���W�[�ŗ������Ă݂���ǂ����ȁH
 *
 * ���Ȃ݂ɂ��́u�{�[���v�̓v���O���}�̔]�݂���JVM���ǂ�����͈͂ł����ł����₹��B
 * ���Ƃ��΁A���́u�{�[���v��2�ɂ���΁A����Ȃ��Ƃ��ł���B
 * �u�{�[��1�v�͕ϐ�X�̓ǂݏ����ɂ܂�镔���̃��b�N�Ɏg���A
 * �u�{�[��2�v�͕ϐ�Y�̓ǂݏ����ɂ܂�镔���̃��b�N�Ɏg���B
 * setX��getX�Ƃ������\�b�h���u�{�[��1�v�Ń��b�N����΁AX�̓ǂݏ����Ńf�[�^�s�����͋N���炸�A
 * setY��getY�Ƃ������\�b�h���u�{�[��2�v�Ń��b�N����΁AY�̓ǂݏ����Ńf�[�^�s�����͋N����Ȃ��B
 * X�AY�A���ꂼ���set/get������A��́u�{�[���v�ňꌳ�I�ɊǗ��ł��邩��A
 * ����Ȃ�Ƃ��Ă��֗��I
 * :2012/11/22
 * */

/**
 * Graphics�ɂ��`����s���Ƃ��A�����ĕ`���̃R���|�[�l���g�iJFrame,JPanel�Ȃǁj�ɁA
 * Paint���������Ȃ��Ƃ���������邩�ȁH
 * Graphics�N���X�̃��\�b�h���g���ƁA����ɘA����`��Paint���\�����A
 * �]�܂Ȃ��^�C�~���O�ōĕ`�悪�N���Ă��܂��\�������邽�߁B
 * (�������́APaint�͂����A�o�b�t�@�����O�p�̉摜�̕\����p�Ɗ���؂��Đ݌v����Ƃ��H
 * �o�b�t�@�����O�摜���N���X�S�̂̃����o�ϐ��ɂ���΁A�����N���X�Ԃł������ł��邵)
 * */

/**
 * �I�u�W�F�N�g�Q�Ƃɂ܂��Tips�B
 * �I�u�W�F�N�g�w��v���O���~���O�ɂ����āA�N���X�Ԃ͉\�Ȍ���a�����ł��邱�Ƃ��]�܂����͎̂�m�̎����B
 * �����Ă��̊ϓ_����l����ƁA�e�N���X�̉��ŃC���X�^���X�����ꂽ�q�N���XA���A
 * �e�N���X����ĕʂ̎q�N���XB�ɃA�N�Z�X�������ꍇ�A
 * �q�N���XA�͐e�N���X�̎Q�Ƃ�ێ�����ׂ����ǂ����A�c�_���ׂ��_�_�ɂȂ肤��B
 * �i�q�N���XA���e�N���X�̎Q�Ƃ�ێ����邱�ƂŁA�������x�͊m���ɏオ���Ă��܂����߁j
 *
 * ���_���猾���΁A���̗l�Ȏ������s���ꍇ�A�q�N���XA�͐e�N���X�̎Q�Ƃ�ێ������]�܂����B
 * �m���ɓ���̃\�[�X�R�[�h�t�@�C���̒��ɕ����̃N���X�������L���悤�ȃX�^�C���ōs���ꍇ�A
 * �q�N���X������ł��A�e�N���X�́u�^�ɂȂ�v�A�N�Z�X���邱�Ƃ͉\�ł���B
 * ���������̕�@�Őe�N���X�ɃA�N�Z�X�����ꍇ�A�e�N���X���C���X�^���X������Ă���ۏ؂͂Ȃ��B
 * �A�N�Z�X�����u�e�N���X�̌^�v���C���X�^���X������Ă��炸�A�A�N�Z�X��ɃI�u�W�F�N�g�����݂��Ȃ������ꍇ�A
 * NullPointerException����������댯��������B
 * �����q�N���XA���炫����Ɛe�N���X�ɃA�N�Z�X�������̂ł���΁A�ȉ��̎菇�𓥂ނ��Ƃ𐄏�����B
 *
 * 1�D�R���X�g���N�^�Ȃǂ�ʂ��āA�e�N���X���C���X�^���X������
 * 2�D�e�N���X�̃C���X�^���X����A�q�N���XA���C���X�^���X���B
 * ���̍ێq�N���XA�̃R���X�g���N�^�͐e�N���X�̎Q�Ƃ���Ɏ���悤�ɂ��Ă���
 * 3�D�q�N���XA�̒��ł́A�\�ߐe�N���X�̎Q�Ƃ��i�[����^���t�B�[���h�Ƃ��Đ錾���Ă����B
 * �����ăC���X�^���X���̍ہA�R���X�g���N�^�̈�Ƃ��Ă���Ă����e�N���X�̎Q�Ƃ��A
 * ���̎q�N���XA�̃t�B�[���h�Ɋi�[
 * 3'.�����āA�e�N���X���ł͐���𑱍s���A�q�N���XB�̃C���X�^���X�����s���Ă���
 * 4�D�q�N���XB�̃C���X�^���X����A�e�N���X�̎Q�Ƃ��o�R���āA�q�N���XA�͎q�N���XB�̃����o�ɃA�N�Z�X����
 *
 * �Ȃ��A�q�N���XA�́A�e�N���X�̉��ɂ��鑼�̎q�N���XC,D,E�c�c�ɃA�N�Z�X���邱�Ƃ͂��蓾�Ȃ��Ȃ�A
 * �e�N���X�ł͂Ȃ��q�N���XB�̎Q�Ƃ݂̂𓾂�Ƃ����������B
 * ��������ƁA�q�N���XA���e�N���X���Q�Ƃł���Ƃ����A�N�Z�X���𗐗p���A���N���X�Ɉ���������A
 * �Ƃ����G���o�O�̃��X�N��\�ߒׂ���B
 *
 * ���̐݌v�ł�������x�̑Ó����͂��邪�A��������ł��N���X�Ԃ̖����������C�ɂȂ�Ȃ�΁A
 * ���������q�N���XA���q�N���XB�̃����o�ɃA�N�Z�X���Ȃ��Ă��ςނ悤�Ȑ݌v���s���Ƃ����������B
 * �݌v��x��R�[�h�̕ێ琫�E�ǐ��̒ቺ�Ƃ������Ȃǂ������ɋN���肤�邪�A
 * ���̕�@�����s�ł���Ȃ�A���ꂪ��ԍ��{�I�ȉ�����@�Ƃ�����B
 */