package com.cosmicbarrage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import com.cosmicbarrage.DrawEventManager.DrawingOrder;

/**
 * �{�[�����i��N���X�B<br><br>
 * <b>���t�@�N�^�����O</b>
 * �{�[���̏�Ԃ��Ǘ�����t���O�Ƃ��āA�ŏI�I��"isBeingGenaratied"���̗p�B<br>
 * ���݂��̃{�[�����AGameMonitor�N���X��timerTask�ɂ��ballList�ɒǉ�����A<br>
 * EffectDrawer��ballGenerationEffect�ɂ��A<br>
 * �����G�t�F�N�g���������Ă��邩�ǂ�����\���B
 * @author Yuichi Watanabe
 * @since 2012/11/15
 * @version 1.01 2012/12/13 Members Sorted
 * */
public class Ball implements DrawEventListener{

	/**
	 * �t�B�[���h(�����o�ϐ�)
	 * */
	public int myID;
	public int diameter= 16;
	public int x = 320;
	public int y =240;
	public EntityVector vector = new EntityVector(0, 0, 0);
	private CosmicBarrage parentFrame;

	//�{�[���ɂ��āA���t���[�����Ƃɍs���ׂ��^�X�N���s�킹��X���b�h
	public MyTaskPerFrame myTaskPerFrame;

	//�{�[���̊�{���x�B���˂ȂǂŃx�N�g�������������ꍇ�A
	//�Ō�ɕK�����̒l���Z�b�g���邱�ƁB�萔�Ƃ���final�錾����
	public final int BASIC_SPEED = 8;

	//���݂��̃{�[�����j�󂳂�Ă��邩�ǂ�����\���t���O
	public boolean isBroken = false;

	//�{�[�������U����ۂ̃C���[�W
	private BufferedImage blastImage = null;

	//���݃{�[�������������ǂ�����\���t���O�B�f�t�H���g�l��false
	public boolean isBeingGenerated = false;

	/**
	 * �R���X�g���N�^1�B�ʏ퐶�����̃f�t�H���g�R���X�g���N�^�B<br>
	 * �Q�[���J�n���Ɏg��
	 * */
	Ball(CosmicBarrage parentFrameInput, int namedID){
		//���g��ID���A���͒l���猈��
		this.myID = namedID;

		//�e�N���X�ւ̎Q�Ƃ�ێ�
		parentFrame = parentFrameInput;

		//�e�N���X�̎Q�Ƃ��o�R���āA�C�x���g�}�l�[�W���Ɏ��g��o�^
		parentFrame.drawEventManager.addDrawEvent(this, DrawingOrder.MIDDLE);

		//isVisible�𖾎��I��false��
		isBeingGenerated = false;

		diameter = 16;

		Random random = new Random();

		//�{�[���������z�u�B�o�[���S���W�́A���̂܂�16px��ɏo��
		x = parentFrame.bar.x + parentFrame.bar.width / 2 - this.diameter / 2;
		y = parentFrame.bar.y - this.diameter;

		//�x�N�^�[�������_����
		vector.setX(random.nextInt(7) - 3);
		vector.setY(random.nextInt(7) - 3);
		//�i���̉��o�E���h������邽�߂ɁA�x�N�g����Y������0�Ȃ�A-1�ɏ�������
		if (vector.getY() == 0){
			vector.setY(-1);
		}
		vector.setScholar(BASIC_SPEED);

		//�{�[���ɂ��āA���t���[�����Ƃɍs���^�X�N���s�킹��X���b�h�B
		//�����ɂԂ������Ƃ��̃{�[�����]�����A�{�[���ƓG�̏Փ˔���ȂǂȂǁB
		//���e���傫���Ȃ�̂ŁA�����N���X�ł͂Ȃ������N���X�Ő錾
		myTaskPerFrame = new MyTaskPerFrame();

	}//�R���X�g���N�^�I��

	/**
	 * �R���X�g���N�^2�B������̓{�[���̔������W���w��ł���^�C�v�B
	 * �܂��A�쐬���ɂ�isBeingGenerated��false�Ƃ����Ⴂ������
	 * */
	Ball(CosmicBarrage parentFrameInput, int namedID, int Xinput, int Yinput){
		//���g��ID���A���͒l���猈��
		this.myID = namedID;

		//�e�N���X�ւ̎Q�Ƃ�ێ�
		parentFrame = parentFrameInput;

		//�e�N���X�̎Q�Ƃ��o�R���āA�C�x���g�}�l�[�W���Ɏ��g��o�^
		parentFrame.drawEventManager.addDrawEvent(this, DrawingOrder.MIDDLE);

		//������̏ꍇ�AisBeingGenerated��true��
		isBeingGenerated = true;

		diameter = 16;

		Random random = new Random();

		//�{�[���������z�u
		this.x = Xinput;
		this.y = Yinput;

		//�x�N�^�[�������_�����B�������A�ŏ��͕K������ɔ�Ԃ悤�ɂȂ��Ă���
		vector.setX(random.nextInt(7) - 3);
		vector.setY(random.nextInt(4) - 3);
		//�i���̉��o�E���h������邽�߂ɁA�x�N�g����Y������0�Ȃ�A-1�ɏ�������
		if (vector.getY() == 0){
			vector.setY(-1);
		}
		vector.setScholar(BASIC_SPEED);

		//�{�[���ɂ��āA���t���[�����Ƃɍs���^�X�N���s�킹��X���b�h�B
		//�����ɂԂ������Ƃ��̃{�[�����]�����A�{�[���ƓG�̏Փ˔���ȂǂȂǁB
		//���e���傫���Ȃ�̂ŁA�����N���X�ł͂Ȃ������N���X�Ő錾
		myTaskPerFrame = new MyTaskPerFrame();

	}//�R���X�g���N�^�I��


	/*
	 * ���\�b�h�E�����N���X�̗񋓖@��
	 *
	 * �ȉ��A���\�b�h����ѓ����N���X��񋓂���B
	 * �񋓂��鏇�Ԃ�
	 *
	 * 1.�C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h
	 * 2.�ʏ�̃��\�b�h
	 * 3.�����N���X
	 * 4.�f�X�g���N�^���\�b�h
	 *
	 * �̏��Ƃ���B
	 * 1,2,3�̃O���[�v���ł́A���ꂼ��X��ABC���Ń����o������ł���B
	 * �I�[�o�[���C�h���ꂽ���\�b�h�́A���������Ȃ������瑽�����ɕ��ׂ�B
	 *
	 * �܂��A�ʏ�̃��\�b�h�ɂ��āA�R�[�h�̉ǐ����グ��ړI�ŁA�u�T�u���\�b�h�v���쐬�B
	 * �{�v���W�F�N�g�ɂ����āA�u�T�u���\�b�h�v�Ƃ́A
	 * �w����ʏ�̃��\�b�h�ɂ��āA�R�[�h�̕ێ琫�E�ǐ����グ��ړI�ŁA
	 * ���ȏ�̕��G�������������X�ɐ؂�o�������\�b�h�x�ƒ�`����B
	 * ���̓�����A�T�u���\�b�h�͌����Ƃ��Ĉ�̃��\�b�h���炵���A�N�Z�X����Ȃ��B
	 * ����āA�N���X�̃u���b�N�{�b�N�X���������邽�߂ɁA
	 * �A�N�Z�X�C���q�ɂ͕K��"private"���w�肷�邱�ƁB
	 * �܂��A�T�u���\�b�h�ł��郁�\�b�h�́A�ʏ�̃��\�b�h����i�K�[���C���f���g�������ċL�q�����B
	 * �\�[�X�R�[�h���ł̃T�u���\�b�h�̈ʒu�́A�Ăяo�����̃��\�b�h�̒����ł���B
	 * ���̏ꍇ�A�T�u���\�b�h�͒ʏ탁�\�b�h��ABC���̕��тɂ͏]�킸�A
	 * �Ăяo�������\�b�h�Ŏg���鏇�ɋL�q���s����B
	 * */

	/* ##############################
	 * 1.�C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h
	 * ##############################*/

	/**
	 * �{�[���̕`��C�x���g
	 * */
	public void onDrawEvent(Image targetImage) {
		// TODO Auto-generated method stub

		//���̎��_��isVisible��true�Ȃ狭�����^�[�����A�`����~
		if (isBeingGenerated == true ) {
			return;
		}


		//�������^�[������Ȃ��Ȃ�A�n���ꂽ�C���[�W��Graphics���擾
		Graphics g = targetImage.getGraphics();

		//�����{�[�����j�󂳂�Ă���Ȃ�A�j��G�t�F�N�g�̕`���ʃ��\�b�h�ōs���A
		//�t���[�����Ƃ̕`�挋�ʂ�Ԃ��Ă��炤
		if ( (isBroken == true) && (blastImage != null) ) {
			//blastImage�̕`����W���v�Z
			Point imagePoint = getJustLocationForImage(96, 96);

			//blastImage��`��
			g.drawImage(blastImage,
						imagePoint.x,
						imagePoint.y,
						imagePoint.x + 96,
						imagePoint.y + 96,
						0, 0, 96, 96, parentFrame);

		}
		//��L�̓�������̂�����ɂ����Ă͂܂�Ȃ��Ȃ�A�ʏ�ʂ�{�[����`��
		else{
			g.setColor(Color.BLACK);
			g.fillOval(this.x, this.y, this.diameter, this.diameter);
		}

		g.dispose();
	}

	/* ############
	 * 2.�ʏ�̃��\�b�h
	 * ############*/

	/**
	 * �{�[����enemy�ƂԂ������ۂɁA���˂ɂ��x�N�g�����ς��邽�߂̃��\�b�h�B
	 * �����蔻�肪���������u�Ԃ̓G���S���W�ƁA�{�[�����S���W�̈ʒu�֌W����A
	 * x�����A��������y�����̃x�N�g�����t�]������B
	 * �Ȃ��A�����ɂ������̓G�ƂԂ���A�x�N�g�����]��������N���āA
	 * ����- * - = +�̖@���Ńx�N�g�����]���N���炸�A�{�[�������̂܂ܒ��i����s�s����
	 * �΍���{�����߁Asynchronized�ȃ��\�b�h�Ƃ���
	 * */
	private synchronized void changeVectorByEnemy(Rectangle targetRect) {
		//�܂��A�{�[���̒��S���W�����߂�
		Point ballCenter
		= new Point((this.x + this.diameter / 2),
					(this.y + this.diameter / 2));

		//���ɁA�G�̒��S���W�����߂�
		Point enemyCenter
		= new Point((targetRect.x + targetRect.width / 2),
					(targetRect.y + targetRect.height / 2));

		//���̂Ƃ��A�{�[���̒��S���W�ƓG�̒��S���W��x�Ay�����ɂ��āA���̐�Βl�����߂�
		int xDifference = Math.abs(ballCenter.x - enemyCenter.x);
		int yDifference = Math.abs(ballCenter.y - enemyCenter.y);

		//����x�����̍��W�����傫���Ȃ�A�{�[����x�����̍��W���t�]
		if (xDifference > yDifference) {
			this.vector.setX(this.vector.getX() * (-1));
		}
		//����y�����̍��W�����傫���Ȃ�A�{�[����y�����̍��W���t�]
		else if (xDifference < yDifference) {
			this.vector.setY(this.vector.getY() * (-1));
		}
		//���������Ƃ����S�ɓ����Ȃ�Ax,y�������t�]
		else {
			this.vector.setX(this.vector.getX() * (-1));
			this.vector.setY(this.vector.getY() * (-1));
		}

		//�����āAvector�̃X�J���[�����l��
		this.vector.setScholar(BASIC_SPEED);

	}//changeVectorByEnemy���\�b�h�I��

	/**�{�[�������������𖞂����Ă��邩�ǂ������m���߂郁�\�b�h�B
	 * ���݂̃{�[���̒��S���W����ʊO�ɂ��邩�ǂ������`�F�b�N���A
	 * ���̏������������ꂽ�ꍇ�{�[���͎�������B
	 * �Ȃ��A�{�[������ʊO�ɏo�邱�Ƃ́A�o�[�ɂ��ł��Ԃ������s�����Ƃ��݂̂Ȃ̂ŁA
	 * ���̃��\�b�h�͉�ʂ̉������ɂ͂ݏo�����ǂ����݂̂��L���b�`����d�l�Ƃ���B
	 * ���̃��\�b�h�ł̃`�F�b�N�ɂ��AisBroken��true�ɂȂ�����A���̏u��
	 * blastImage��`�悷��ʃX���b�h���A�����N���X�Ƃ��Ĕ��s����*/
	private void checkGoOutFromDisplay() {
		// TODO Auto-generated method stub

		Point ballCenter = getCenterLocation();

		//�������̍��W����ʊO�ɂ������ꍇ�A���̏u��isBroken�t���O��true�ɂȂ�A
		//onDrawEvent���\�b�h�ł�drawBreakDown���`�悳��n�߂�
		if ( (ballCenter.y > parentFrame.outputScreen.getHeight()) ) {

			//isBroken�t���O��true��
			isBroken = true;

			//blastImageThread�𔭍s
			BlastImageThread blastImageThread = new BlastImageThread();
			blastImageThread.start();

		}

	}

	/**
	 * �{�[�������̃t���[���ɂ����āA�G�Ƃ̓����蔻�肪�������Ă��Ȃ������`�F�b�N���郁�\�b�h�B<br>
	 * enemyList�ɓo�^����Ă���enemy�C���X�^���X���Ɠ����蔻��̗L�����`�F�b�N���A<br>
	 * ���������蔻�肪�������ꍇ�A����enemy�C���X�^���X�̎������\�b�h���Ăяo���A<br>
	 * �{�[�����g�̓x�N�g���𔽓]������B<br>
	 * �Ȃ��A��̃{�[����1�t���[���ɂ��A1�̂̓G�Ƃ����Փ˂��N�����Ȃ����̂Ƃ���B<br>
	 * (1��ł��G��breakDown���\�b�h���g���K�[������A<br>
	 * break�Ń��\�b�h����for�߂��狭���E�o)<br>
	 * �������Ȃ��ƁA���̃t���[���ɂ����ă{�[���̃q�b�g���󂯂Ă��Ȃ��G���A<br>
	 * �u�U���v���N�����o�O���������鎞�����邽��
	 * */
	private void checkHitWithEnemy() {

		//���ݓo�^����Ă���G�C���X�^���X���Ɠ����蔻������s
		for (int i = 0; i < parentFrame.enemyHive.size(); i++){
			//�R�[�h�̉ǐ����グ�邽�߂ɁA���̃��[�v�Ō��Ă���G�C���X�^���X�̎Q�Ƃ����ϐ��Ɋi�[
			Enemy checkingEnemy = parentFrame.enemyHive.get(i);
			
			if (checkingEnemy == null) {
//				System.out.println("Enemy i = " 
//						+ String.valueOf(i) + " is null");
				continue;
			}

			//�Ȃ��A���̎��_��enemy�ɐ����t���O�������Ă��Ȃ��Ȃ�Acontinue�Ŕ�����΂�
			//(���łɔj�󂳂ꂽ�G�ɓ����蔻��͂Ȃ�����)
			try {
				if (checkingEnemy.isAlive == false){
					continue;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}

			//checkingEnemy�̃����o���A���݃X�L�������Ă���enemy�̓����蔻�肪
			//��������̈���v�Z
			Rectangle hittableRegion
			= new Rectangle(
					checkingEnemy.x + checkingEnemy.hitCheckingOffset,
					checkingEnemy.y + checkingEnemy.hitCheckingOffset,
					checkingEnemy.width - checkingEnemy.hitCheckingOffset * 2,
					checkingEnemy.height - checkingEnemy.hitCheckingOffset * 2
					);

			//�����Ōv�Z�����̈�ƁA���݂̃{�[���̍��W���ꕔ�ł��d�Ȃ��Ă���΁A
			//�����蔻�肪���������Ƃ݂Ȃ��B

			//�����蔻������s�B���e�͉ǐ��̊ϓ_����ʃ��\�b�h�ɐ؂�o��
			boolean isHit = true;//�����l��true�ɃZ�b�g
			isHit = isHitWithThisRegion(hittableRegion);

			//���̃t���O��true�Ȃ�A����enemy�ƃ{�[���̓����蔻�肪
			//�������Ă���Ƃ݂Ȃ��A�Y����enemy�̔j�󃁃\�b�h���N����A
			//�{�[����enemy�̏Փˊp�x�ɉ����āA�{�[���̃x�N�g����ω�������B
			//�Ō�ɁAbreak��for�߂�E�o
			if (isHit == true){
				checkingEnemy.breakDown();
				this.changeVectorByEnemy(hittableRegion);
				break;
			}


		}//for�ߏI��


	}//checkHitWithEnemy���\�b�h�I��

	/**
	 * �{�[�������ݕǂ��o�[�Ƀq�b�g���Ă��邩���`�F�b�N���A�q�b�g���Ă���Ȃ�
	 * x�����Ay�����̃x�N�g�������ꂼ�ꔽ�]�����郁�\�b�h
	 * */
	private void checkReflectionByWall(){
		//�ǂƂ̏Փ˂ɂ��X�̔��]
		if ((x + diameter) >= 640){
			vector.setX(Math.abs(vector.getX()) * (-1));
		}
		else if ((x + diameter) <= diameter){
			vector.setX(Math.abs(vector.getX()));
		}

		//�ǂƂ̏Փ˂ɂ��y�̔��]
		if ((y + diameter) <= diameter){
			vector.setY(Math.abs(vector.getY()));
		}
		else if ((y + diameter) >= 448){

			//�o�[�ɂ��ł��Ԃ��̐��۔���
			if (isBallHitByBar() == true){
				setRefrectionAngle();
			}
			//�Ȃ��A����if�߂�ʂ�Ȃ��Ƌ�����ʂ̉��ɂ͂ݏo��
			//���{�[������������
		}

	}

	/**�{�[���̒��S���W��Ԃ����\�b�h�B<br>
	 * �I�[�o�[���C�h�𗘗p���A�I�v�V�����Ƃ���"LimitsInDisplay"�t���O���w��\�B<br>
	 * �������̃t���O��"true"�Ɏw�肵���ꍇ�A�{�[���̌��݂̍��W�Ɋւ�炸�A<br>
	 * �Œ�ł�(0,0)�A�ō��ł���ʂ̍����E���ɓ��������W�����Ԃ��Ă��Ȃ��Ȃ�*/
	public Point getCenterLocation(){
		int centerX = this.x + this.diameter / 2;
		int centerY = this.y + this.diameter / 2;
		Point point = new Point(centerX, centerY);

		return point;
	}
	/**�{�[���̒��S���W��Ԃ����\�b�h�B<br>
	 * �I�[�o�[���C�h�𗘗p���A�I�v�V�����Ƃ���"LimitsInDisplay"�t���O���w��\�B<br>
	 * �������̃t���O��"true"�Ɏw�肵���ꍇ�A�{�[���̌��݂̍��W�Ɋւ�炸�A<br>
	 * �Œ�ł�(0,0)�A�ō��ł���ʂ̍����E���ɓ��������W�����Ԃ��Ă��Ȃ��Ȃ�*/
	public Point getCenterLocation(boolean limitsInDisplay){
		int centerX = this.x + this.diameter / 2;
		int centerY = this.y + this.diameter / 2;

		//�������̎��_��centerX,Y��0�������A��ʃT�C�Y�ȏ�Ȃ�A���ꂼ�ꉺ���l��
		//����l�ɐ��l��␳

		if (limitsInDisplay == true) {
			//X�̕␳
			if (centerX < 0) {
				centerX = 0;
			} else if (centerX > parentFrame.outputScreen.getWidth()) {
				centerX = parentFrame.outputScreen.getWidth();
			}
			//Y�̕␳
			if (centerY < 0) {
				centerY = 0;
			} else if (centerY > parentFrame.outputScreen.getHeight()) {
				centerY = parentFrame.outputScreen.getHeight();
			}
		}

		Point point = new Point(centerX, centerY);

		return point;
	}

	/**�{�[���ɉ��炩�̉摜���d�˂�ہA���̏d�˂�摜�̓K���ȍ��W��Ԃ����\�b�h�B<br>
	 * �摜�̕��ƍ����������Ɏ󂯁A�{�[���̌��݂̒��S���W����A<br>
	 * �摜���\�������ׂ��N�_��x�Ay���W��ԋp����B<br>
	 * ���̃��\�b�h�̎w�肵�����W���A�d�˂�摜�̋N�_�Ɏw�肵���ꍇ�A<br>
	 * �{�[���Ɖ摜�̒��S���s�b�^������
	 * */
	public Point getJustLocationForImage(int width, int height){
		//�摜�̍��W
		Point imagePoint = new Point();
		//���݂̃{�[���̒��S���W�BlimitsInDisplay�I�v�V������true�Ɏw��
		Point ballPoint = getCenterLocation(true);

		//�摜�̍��W�́A�{�[���̒��S���W����A���̉摜�̕�/2�A����/2�����������̂ɓ�����
		imagePoint.setLocation(ballPoint.x - width / 2, ballPoint.y - height / 2);

		return imagePoint;

	}

	/**
	 * �{�[���ƃo�[�́u������v������s�����\�b�h�B
	 * �{�[�����o�[��X���W�̈ʒu�֌W�œ����肩�O�ꂩ���肷��̂ŁA
	 * �{�[����Y���W����ʃT�C�Y-�{�[�����a�ɒB�����^�C�~���O�ł�����g������
	 * */
	private boolean isBallHitByBar(){

		//�{�[�����o�[�́u�����v�ɗ������ꍇ
		if ((this.diameter + this.x) < parentFrame.bar.x){
			return false;
		}
		//�{�[�����o�[�́u�E���v�ɗ������ꍇ
		else if((parentFrame.bar.x + parentFrame.bar.width) < this.x){
			return false;
		}
		//�{�[���ƃo�[���u�q�b�g�v�����ꍇ
		else{
			return true;
		}

	}

	/**
	 * �w�肳�ꂽ��`�̈�ƁA���̃{�[�����d�Ȃ��Ă��邩�ǂ����𔻒肵�A���̌��ʂ�Ԃ����\�b�h�B
	 * */
	private boolean isHitWithThisRegion(Rectangle targetRect){
		//������true�̃t���O���������񗧂āAif�߂Ŕ���B
		//�r���̑o���̍��W�`�F�b�N�ŁA�u�q�b�g���Ă��邱�Ƃ����肦�Ȃ��v�������������ꍇ�A
		//�����ɂ��̃t���O�͐܂��
		boolean isHit = true;

		//�{�[�����G�̍����ɍs���߂��Ă���ꍇ
		if (this.x + this.diameter <= targetRect.x) {
			isHit = false;
		}
		//�{�[�����G�̏㑤�ɍs���߂��Ă���ꍇ
		else if (this.y + this.diameter <= targetRect.y) {
			isHit = false;
		}
		//�{�[�����G�̉����ɍs���߂��Ă���ꍇ
		else if (targetRect.y + targetRect.height <= this.y) {
			isHit = false;
		}
		//�{�[�����G�̉E���ɍs���߂��Ă���ꍇ
		else if(targetRect.x + targetRect.width <= this.x) {
			isHit = false;
		}
		return isHit;
	}

	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}

	/**���݂�x�����Ay�����̃x�N�g���̕������A�{�[���̈ʒu���ړ������郁�\�b�h*/
	private void moveBall() {
		x = x + vector.getX();
		y = y + vector.getY();
	}

	/**
	 * �o�[�ɂ��{�[���̑ł��Ԃ������������ꍇ�A�{�[���̒��˕Ԃ�p�x��V���Ɍ��肷�郁�\�b�h�B<br>
	 * �ł��Ԃ��̐������m�肵���u�ԂɎg�����ƁB<br>
	 * �Ȃ��A�o�[�ɂ��ł��Ԃ������������ꍇ�A<br>
	 * ���m���Œʏ�̃{�[�����˂ł͂Ȃ��u�G�C���h���t���N�V�����v����������B<br>
	 * �u�G�C���h���t���N�V�����v�̔����m���͎c��̓G�̐��ɔ���Ⴕ�č����Ȃ�B
	 * */
	private void setRefrectionAngle(){

		//�܂��̓G�C���h���t���N�V���������̔���B
		//�G�C���h���t���N�V�����̓Q�[���J�n���ɂ͂قƂ�ǔ������Ȃ����A
		//�G��������Ό���قǔ����m�����オ��A�ŏI�I�ɂ͖�3/4��̊m���Ŕ���
		double finalProbability = 0.75;//���ꂭ�炢���m���̕������������H
		double presentProbability = finalProbability
									/ Math.pow(1.05, (parentFrame.enemyHive.size() - 1));
		Random random = new Random();

		//�G�C���h���t���N�V���������������ꍇ
		if (random.nextDouble() <= presentProbability){
			doAimedReflection();
		}
		//�ʏ�̔��˂��N�����ꍇ
		else{
			doNormalReflection();
		}

	}

	/**
	 * �o�[�ɂ��ʏ�̔��˂��s�����\�b�h�B<br>
	 * �o�[�ƃ{�[���̈ʒu�֌W�ɂ��A�ǂ̕����Ɍ����Ĕ��˂���邩�����܂�B<br>
	 * �V���ȃx�N�g���ʂ̑g�ݍ��킹��(a, b)(a,b=-3~+3�܂ł̊e�l)�ł���B<br>
	 * �Ȃ��A�Q�[���ɂ����Ē���Ԃ̔�����h���ړI�ŁA�o�[�ɂ��ł��Ԃ����A<br>
	 * �����_���Ƀ{�[����x���W��-2~+2�����u����d�l���̗p���Ă���
	 * */
	private void doNormalReflection(){
		//�V���ȃx�N�g����x,y����
		int newX = 0;
		int newY = 0;

		//�����āA���݂̃{�[���́u�w�\�v��x���W���Z�o
		int BallCenter_X = this.x + this.diameter / 2;

		//���ɁA�o�[�̌��݂�x���W�ƒ������擾
		int Bar_X = parentFrame.bar.x;
		int BarLength = parentFrame.bar.width;

		//��������傫�߂�if�߁Bbar��x���W�A�����A�����ă{�[�����S��x���W�ɂ��A
		//�x�N�g�������߂���

		//�o�[�́u�c�v�����͔p�~�B�u�c�v����������ƁA�u�c�v�Ƀ{�[���𓖂đ�����
		//�Q�[��������ԂɂȂ��Ă��܂��ꍇ�����邽��

		//�o�[�̍��[~������10%�̕����Ƀq�b�g�����ꍇ
		if(BallCenter_X < (Bar_X + BarLength / 10 * 1)){
			newX = -3;	newY = -1;
		}
		//�o�[�̒�����10%~20%�̕����Ƀq�b�g�����ꍇ
		else if(BallCenter_X < (Bar_X + BarLength / 10 * 2)){
			newX = -2;	newY = -1;
		}
		//�o�[�̒�����20%~30%�̕����Ƀq�b�g�����ꍇ
		else if(BallCenter_X < (Bar_X + BarLength / 10 * 3)){
			newX = -1;	newY = -1;
		}
		//�o�[�̒�����30%~40%�̕����Ƀq�b�g�����ꍇ
		else if(BallCenter_X < (Bar_X + BarLength / 10 * 4)){
			newX = -1;	newY = -2;
		}
		//�o�[�̒�����40%~50%�̕����Ƀq�b�g�����ꍇ
		else if(BallCenter_X < (Bar_X + BarLength / 10 * 5)){
			newX = -1;	newY = -3;
		}
		//�o�[�̒�����50%~60%�̕����Ƀq�b�g�����ꍇ
		else if(BallCenter_X < (Bar_X + BarLength / 10 * 6)){
			newX = 1;	newY = -3;
		}
		//�o�[�̒�����60%~70%�̕����Ƀq�b�g�����ꍇ
		else if(BallCenter_X < (Bar_X + BarLength / 10 * 7)){
			newX = 1;	newY = -2;
		}
		//�o�[�̒�����70%~80%�̕����Ƀq�b�g�����ꍇ
		else if(BallCenter_X < (Bar_X + BarLength / 10 * 8)){
			newX = 1;	newY = -1;
		}
		//�o�[�̒�����80%~90%�̕����Ƀq�b�g�����ꍇ
		else if(BallCenter_X < (Bar_X + BarLength / 10 * 9)){
			newX = 2;	newY = -1;
		}
		//�o�[�̒�����90%���E�̕����Ƀq�b�g�����ꍇ
		else{
			newX = 3;	newY = -1;
		}

		//�ȏ�̃V�[�N�G���X�œ���ꂽmyScholar�AnewX�AnewY�����ƂɁA
		//���̃{�[���̐V����vector��ݒ�
		this.vector.setX(newX);
		this.vector.setY(newY);
		this.vector.setScholar(BASIC_SPEED);//�Ō�ɃX�J���[��ݒ肷��̂��~�\

		//�Ō�ɁA�{�[����x���W�������_����-2~+2�����u��������
		Random random = new Random();
		this.x = this.x + (random.nextInt(5) - 2);
		random = null;//random�C���X�^���X�𖾎��I�ɔj��
	}

	/**
	 * �G�C���h���t���N�V�����ɂ�锽�˂��s�����\�b�h�B<br>
	 * �G�C���h���t���N�V�����Ƃ́A�{�[���̑ł��Ԃ��̏u�Ԃɑ��݂���A<br>
	 * �����Ă���G��_���ă{�[�������ˊp���߂邱�Ƃ������B<br>
	 * �Q�[���㔼�A�G�̐����܂΂�ɂȂ��ă{�[�����G�ɓ�����ɂ����Ȃ����ہA<br>
	 * �Q�[����i�s�����₷�����邽�߂̏��u�ł���B
	 * */
	private void doAimedReflection(){
		//�G�C���h���t���N�V���������������u�ԂɁA
		//����I���S�ł��Ă����ꍇ�ɔ����A�S�G�̐������m�F�B
		//�������̎��_�œG���S�ł��Ă�����A����ɒʏ�̑ł��Ԃ��������s���A
		//�����ċ������^�[��
		boolean areAllEnemiesDied = true;
		for (int i = 0; i < parentFrame.enemyHive.size(); i++) {
			//��̂ł��G�������Ă���΃t���O�͐܂��
			if (parentFrame.enemyHive.get(i).isAlive == true) {
				areAllEnemiesDied = false;
				break;
			}
		}

		//���̎��_�Ńt���O���܂�Ă��Ȃ��Ȃ�A�m�[�}�����˂��N�����ċ������^�[��
		if (areAllEnemiesDied == true) {
			doNormalReflection();
			return;
		}

		//�����܂ŏ������i�񂾂Ȃ�A���悢��G�C���h���t���N�V�������{�����B
		//�܂��͓G�̒��Ő����Ă�����̂��A�����_���ɑI��
		Random random = new Random();
		int chosenID = 0;
		Enemy targetEnemy = null;

		while (true) {
			chosenID = random.nextInt(parentFrame.enemyHive.size());

			if (parentFrame.enemyHive.get(chosenID).isAlive == true) {
				targetEnemy = parentFrame.enemyHive.get(chosenID);
				break;
			}

		}

		//�^�[�Q�b�g�ɂȂ�G�����肳�ꂽ�Ȃ�A(�G�̒��S���W)-(���̃{�[���̒��S���W)��
		//���ʂ�V�����x�N�g���ɂ��A�X�J���[��W�����x�ɂ��ďI��
		int newVectorX = targetEnemy.getCenterLocation().x - this.getCenterLocation().x;
		int newVectorY = targetEnemy.getCenterLocation().y - this.getCenterLocation().y;

		this.vector.setX(newVectorX);
		this.vector.setY(newVectorY);
		this.vector.setScholar(BASIC_SPEED);

		//�Ō�ɁA�e�t���[����EffectDrawer����G�t�F�N�g���N��
		parentFrame.effectDrawer.aimedReflectionEffect(
				this.getCenterLocation().x, this.getCenterLocation().y);

	}

	/* ##########
	 * 3.�����N���X
	 * ##########*/

	/**�{�[������������G�t�F�N�g��������AThread�N���X���p�����������N���X�B
	 * 2000ms(120�t���[��)�����āAblastImage�փ{�[�������X�̗��q�ɍӂ��U��C���[�W��`��������B
	 * �C���[�W�̑傫����96*96px�Ƃ���B
	 * �Ȃ��AonDrawEvent�ɓo�^���ꂽ�ȍ~�̕`��C�x���g���~�߂Ȃ����߂ɁA
	 * �ʃX���b�h�𔭍s���Ĕ����̕`����s���B
	 * �܂��A���̃X���b�h���I���Ɠ����ɁA���g��e�N���X��ballList����폜����*/
	private class BlastImageThread extends Thread{

		//���̃N���X�̃X���b�h�𐶑������鎞��
		int timeCount = 2000;

		//�����o�ϐ��B���U���闱�q�̍��W�ƃx�N�g�����L�^����B
		//���q����256����A���̃N���X�̃R���X�g���N�^�����s�����ہA
		//�������W�ƃx�N�g���������_���Ɍ��肳���
		Point particleLocation[] = new Point[255];
		EntityVector particleVector[] = new EntityVector[255];

		//����͍��W�ƃx�N�g�����R���X�g���N�^�Ō��肷��B
		//�Ȃ��A���̃N���X�̃X�[�p�[�N���X�ł���Thread�N���X�̃R���X�g���N�^�́A
		//Java�̎d�l�ケ�̃N���X�̃R���X�g���N�^�̎��s�O�ɁA
		//�f�t�H���g�R���X�g���N�^���������s�����̂ŐS�z���p�B
		//�����I�ɃX�[�p�[�N���X�̃R���X�g���N�^�����s�������Ȃ�A"super();"�Ɛ錾
		BlastImageThread(){
			//����͗Ꭶ�Ƃ��āA�X�[�p�[�N���X�̃R���X�g���N�^�𖾎��I�Ɏ��s�B
			//�Ȃ������I�Ɏ��s����ꍇ�Asuper();�̓T�u�N���X�̃R���X�g���N�^�̍ŏ���
			//�s��Ȃ��ƃR���p�C���G���[�ɂȂ�
			super();

			//��������A�e�탁���o�̏��������J�n

			//timeCount��2000��ݒ�
			timeCount = 2000;

			//�܂���random���C���X�^���X��
			Random random = new Random();

			//particleLocation�́Ax,y�����ꂼ��41~55px�̂����ꂩ�ɂȂ�
			for (int i = 0; i < particleLocation.length; i++) {
				particleLocation[i]
				= new Point(random.nextInt(16) + 40, random.nextInt(16) + 40);

				//�����āA���̗��q�̃x�N�g��������Bx <= 48 ��x�̓}�C�i�X�A�����łȂ��Ȃ�+�A
				//y <= 48��y�̓}�C�i�X�A�����łȂ��Ȃ�+
				//�x�N�g���������_�������邽�߂ɁA1~10�̂����ꂩ�̒l�������_���擾

				//�܂��̓x�N�g�����g��������
				particleVector[i] = new EntityVector(0, 0, 0);

				//x������
				if (particleLocation[i].x <= 48) {
					particleVector[i].setX((random.nextInt(10) + 1) * (-1) );
				}else {
					particleVector[i].setX(random.nextInt(10) + 1 );
				}
				//y������
				if (particleLocation[i].y <= 48) {
					particleVector[i].setY((random.nextInt(10) + 1) * (-1) );
				}else {
					particleVector[i].setY(random.nextInt(10) + 1);
				}

				//�x�N�g���̃X�J���[�́A2,4,8,16�̂����ꂩ�������_���I���B(2^n���g��)
				//�Ȃ��A���t���[�����Ƃɂ��ꂾ���̃s�N�Z�����i�܂���Ɨ��q�̔�U���x�����߂���̂ŁA
				//run���\�b�h�ňړ����x�̒������s���Ă���(�ړ��p�x��200ms��1��ɂ��Ă���)
				int power = random.nextInt(4) + 1;//�w���̏搔
				particleVector[i].setScholar((int)Math.pow(2, power));

			}//for�ߏI��

		}//�R���X�g���N�^�I��

		//�R���X�g���N�^�ŗp�ӂ��ꂽ�p�����[�^�����ƂɁA���q��blastImage�C���X�^���X�ɕ`������ł����B
		//���q�̃x�N�g���̑傫���̊֌W����A���q�̈ړ��p�x��200ms��1��Ƃ���(�܂�A10�񂾂��X�V)
		@Override
		public void run(){

			//���݊i�[���ꂽ���q�̍��W�ɁA���q��`��B
			//timeCount��0�ɂȂ�܂Ń��[�v
			while (timeCount >= 0) {
				//blastImage��������
				blastImage = new BufferedImage(96, 96, BufferedImage.TYPE_INT_ARGB);

				//blastImage��Graphics���擾
				Graphics g = blastImage.getGraphics();
				g.setColor(Color.BLACK);

				//���q��for�߂ŕ`��
				for(int i = 0; i < particleLocation.length; i++) {
					g.fillRect(particleLocation[i].x, particleLocation[i].y, 1, 1);
				}

				//���ɁA200ms�����ŋN���鏈���B
				//�eparticleLocation���A���̃x�N�g����������
				for(int i = 0; i < particleLocation.length; i++) {
					particleLocation[i].x
					= particleLocation[i].x + particleVector[i].getX();
					particleLocation[i].y
					= particleLocation[i].y + particleVector[i].getY();
				}

				g.dispose();

				//200ms�����X���[�v
				try {
					Thread.sleep(200);
					//timeCount��200�������炷
					timeCount = timeCount - 200;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}//�������[�v�I��

			//���[�v�𔲂���run���I��钼�O��blastImage��null�ɂ���
			blastImage = null;

			//�����ăf�X�g���N�^���N��
			destructBall();

		}//run���\�b�h�I��
	}//BlastImageThread�I��

	/**
	 * ���t���[�����Ƃɍs���ׂ��A�{�[���ɂ܂��^�X�N����{���������́B
	 * �ǂ�o�[�Ƃ̏Փ˔���A�G�Ƃ̓����蔻��ȂǁB
	 * �Ȃ��A�e��^�X�N�́A�^�X�N����ƂɃ��\�b�h�Ƃ��Đ؂�o���Ă���
	 * */
	class MyTaskPerFrame extends Thread{
		@Override
		public void run(){
			//���[�v�J�n���O�A����isBeingGenerated��true�Ȃ炱���false��
			if (isBeingGenerated == true) {
				isBeingGenerated = false;
			}

			//�{�[�����j�󂳂�Ȃ��ԁA�ȉ��̏����𖳌����[�v
			while(isBroken == false){
				//��ʉ��ɂ͂ݏo�Ď��������𖞂����Ă��Ȃ����`�F�b�N
				checkGoOutFromDisplay();

				//�ȉ��̏�����Ball�������Ă��Ȃ��ƍs���Ȃ��̂ŁA
				//���̎��_��isBroken��true�Ȃ烋�[�v�������E�o
				if (isBroken == true) {
					break;
				}

				//�ǂ�o�[�Ƃ̏Փ˔���&�x�N�g�����]���\�b�h
				checkReflectionByWall();

				//�G�Ƃ̏Փ˔���&�x�N�g�����]���\�b�h
				checkHitWithEnemy();

				//�{�[���̈ړ����\�b�h
				moveBall();

				try {
					sleep(16);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//try�ߏI��

			}//���[�v�I��

		}//run���\�b�h�I��


	}//MyTaskPerFrame�I��

	/* ###############
	 * 4.�f�X�g���N�^���\�b�h
	 * ###############*/

	/**
	 * ���̃C���X�^���X���g��j�󂷂郁�\�b�h�B<br>
	 * ��̓I�ɍs���鏈���͈ȉ��̒ʂ�B<br><br>
	 * 1.isBroken�t���O��true�ɃZ�b�g<br>
	 * 2.�e�N���X��ballList����A���g�̓o�^��remove����<br>
	 * 3.�e�N���X��drawEventManager����A���g�̓o�^���O��<br>
	 * 4.�����Ɏ��g�̎Q�Ƃ�null�ɂ���<br>
	 * 5.�Ō��GC���蓮�N�����A���S�ɃC���X�^���X�����ł�����<br><br>
	 * <b><i>�x���I</i></b><br>
	 * ���̃f�X�g���N�^���\�b�h����������ɂ�����A�{�[���͂��̃Q�[���Z�b�V������ʂ��āA<br>
	 * �K����ӓI��ID���U����悤�Ɏ������邱�ƁB<br>
	 * ����������ƁA��x�g��ꂽ�{�[��ID�́A���Ƃ����̃{�[�����j�󂳂ꂽ��ł�<br>
	 * ��x�Ǝg���Ȃ��悤�ɂ��邱�ƁB<br>
	 * ���̂Ȃ�A���̃f�X�g���N�^���\�b�h�ł́A�j��̃^�[�Q�b�g�ɂȂ�{�[�����A<br>
	 * ID���L�[�Ƃ��Č�������d�l���̗p���Ă��邽�߁B<br>
	 * ID�Ɉ�Ӑ����ۏ؂���Ȃ��ꍇ�A�������j��Ώۂ̃{�[���������ł����A<br>
	 * ���̃��\�b�h���\�����āA���֌W�ȃ{�[�����j�󂳂�Ă��܂��Ƃ����o�O����������댯������B
	 * */
	public void destructBall() {

		//���g��e�N���X��ballList����T���o���A������폜
		Ball myself = null;
		for (int i = 0; i < parentFrame.ballList.size(); i ++) {
			if (parentFrame.ballList.get(i).myID == myID) {
				myself = parentFrame.ballList.get(i);
				break;
			}
		}
		parentFrame.ballList.remove(myself);

		//�X�ɁA�e�N���X��DrawEventManager����A���g�̓o�^���O��
		parentFrame.drawEventManager.removeDrawEvent(myself);

		//isBroken��true��
		isBroken = true;
		//isBeingGenerated��false��
		isBeingGenerated = false;

		//���g�̎Q�Ƃ𖾎��I�ɔj�����A�K�x�[�W�R���N�^���蓮�N�����Ď��g�����S�ɏ�������
		myself = null;
		System.gc();
	}

}//Ball�N���X�I��
