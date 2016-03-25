package com.cosmicbarrage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.cosmicbarrage.DrawEventManager.DrawingOrder;

/**�Q�[�����ɔ������邳�܂��܂ȃG�t�F�N�g�̕`�����󂯂�N���X�B<br>
 * ���N���X�̃��\�b�h�ȂǂŁA���̃N���X�̃��\�b�h���Ă΂��ƁA<br>
 * �G�t�F�N�g�摜�����̕ʃX���b�h���N������Ɠ����ɁA<br>
 * ���̃N���X��effectImageList�ɁA�Ή�����bufferedImage�ƁA����x,y���W���i�[�����B<br>
 * ����BufferedImage�͕ʃX���b�h�ɓ�����ꂽ���\�b�h�̃��[�v�ɂ��<br>
 * ���Ԃ��Ƃɕω��𑱂��A�ŏI�I�ɃG�t�F�N�g�̕\�����I���������_�ŁA<br>
 * effectImageList�Ɋi�[���ꂽ�f�[�^�Q���j����B<br>
 * ���̃N���X�Ɏ������ꂽonDrawImage�ł́A���t���[�����ƂɁAeffectImageList��<br>
 * �i�[���ꂽBufferedImage��\������
 * @author Yuichi Watanabe
 * @since 2012/12/19
 * @version 1.00
 * */
public class EffectDrawer implements DrawEventListener{

	/*
	 * �����o�ϐ�
	 * */

	//���݂��̃N���X���ێ����Ă���A���̃t���[�����Ƃɕ\���������摜�̓��e�ƁA
	//����x,y���W�B�C���[�W�ƍ��W�f�[�^����̃N���X�Ɉ�{�����ĉ^�p
	private List<EffectImageProperty> effectImageList;

	//�e�N���X�ւ̎Q��
	CosmicBarrage parentFrame;

	/**
	 * �R���X�g���N�^
	 * */
	EffectDrawer(CosmicBarrage parentFrameInput){
		effectImageList = new ArrayList<EffectImageProperty>();

		//�e�N���X�̎Q�Ƃ��Z�b�g
		parentFrame = parentFrameInput;

		//�e�N���X�̎Q�Ƃ���A���g��DrawEventManager�ɓo�^
		parentFrame.drawEventManager.addDrawEvent(this, DrawingOrder.LAST);

	}

	/*
	 * �ȉ��A���\�b�h����ѓ����N���X��񋓂���B
	 * �񋓂��鏇�Ԃ�
	 * 1.�C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h
	 * 2.�ʏ�̃��\�b�h
	 * 3.�����N���X
	 * �̏��Ƃ���B
	 * 1,2,3�̃O���[�v���ł́A���ꂼ��X��ABC���Ń����o������ł���B
	 * �I�[�o�[���C�h���ꂽ���\�b�h�́A�����Ȃ������瑽�����ɕ��ׂ�
	 * */

	/* ##############################
	 * 1.�C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h
	 * ##############################*/

	public void onDrawEvent(Image targetImage) {
		// TODO Auto-generated method stub

		//effectImageList�̒�����0�Ȃ�A�������^�[��
		if (effectImageList.size() == 0) {
			return;
		}

		//�������^�[�����Ȃ��Ȃ�{�����ֈڍs

		//Graphics���擾
		Graphics g = targetImage.getGraphics();

		//����effectImageList�Ɋi�[����Ă���摜��S�āAtargetImage�ɕ`������
		for (int i = 0; i < effectImageList.size(); i++) {
			//�ǐ��̊ϓ_����A���ϐ����g�p
			EffectImageProperty target = effectImageList.get(i);

			//����image��Null�Ȃ�A�o�O�h�~�̖ړI��continue
			if (target.image == null) {
				continue;
			}

			g.drawImage(
					target.image,
					target.x,
					target.y,
					target.x + target.image.getWidth() * target.magnification,
					target.y + target.image.getHeight() * target.magnification,
					0,
					0,
					target.image.getWidth(),
					target.image.getHeight(),
					parentFrame);
		}

		//Graphics��j��
		g.dispose();

	}

	/* ############
	 * 2.�ʏ�̃��\�b�h
	 * ############*/

	/*
	 * �Ȃ��A�ȉ��̃��\�b�h�ɂ͕ʃX���b�h�𔭍s���郁�\�b�h���������A
	 * �����͓��ɒ��L�Ȃǂ��Ȃ��ꍇ�A�ʃX���b�h�̏������e�͖����N���X�ŋL�q���A
	 * �����N���X�͎g�p���Ȃ����̂Ƃ���B
	 * ���̂Ȃ�A�ȉ��̃��\�b�h�ōs�����e�́A�ʃX���b�h���̏����݂̂ŁA
	 * �X���b�h���s�O��ɕʂ̏����͌����Ƃ��ċL�q����Ȃ��B
	 * ����đS�Ă̏������e�𖳖��N���X�ŋL�q���Ă��A�R�[�h�̉ǐ���������
	 * �댯���͒Ⴂ���߂ł���B
	 * */

	/**
	 * �G�C���h���t���N�V�����̃G�t�F�N�g�𔭐������郁�\�b�h�B<br>
	 * �G�C���h���t���N�V���������������u�Ԃ̃{�[�����S���W����A<br>
	 * "AIMED REFLECTION"�̕������|�b�v�A�b�v����B
	 * ��́A�G�C���h���t���N�V�����摜�̔����N�_�ɂȂ��Ăق������S���W
	 * */
	public void aimedReflectionEffect(int centerX, int centerY) {
		/* #############
		 * �e��f�[�^�̗p��
		 * #############*/

		//Aimed Reflection�̉摜�����[�h
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResource("AimedReflection.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//���̃��[�h�����摜�̕��ƍ�������A�摜�`��̊�_�ƂȂ鍶����W������
		int x = centerX - img.getWidth() / 2;
		int y = centerY - img.getHeight() / 2;

		//�摜�g�嗦�B2�{���w��
		int mag = 2;

		//effectImageList�ɁA�C���[�W��o�^
		final EffectImageProperty myImageProperty = new EffectImageProperty(img, x, y, mag);
		effectImageList.add(myImageProperty);

		/* #############
		 * �����N���X�̔��s
		 * #############*/
		Thread thread = new Thread(){
			int frameNumber = 0;

			@Override
			public void run(){

				while(frameNumber < 60) {
					myImageProperty.y = myImageProperty.y - 1;

					frameNumber = frameNumber + 1;

					//�ŏI�t���[���ŁA���g�̓o�^��j��
					if (frameNumber >= 60) {
						effectImageList.remove(myImageProperty);
					}

					//1�t���[���X���[�v
					try {
						Thread.sleep(16);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}//���[�v�I��


			}//run���\�b�h�I��

		};//Thread�̖����N���X�I��

		thread.start();


	}

	/**
	 * �{�[�������������ۂ̃G�t�F�N�g�𔭐������郁�\�b�h�B<br>
	 * 2000ms(��120�t���[��)�̊ԁA64*64�͈̔͂ɕ`�悳�ꂽ�����̉~���A<br>
	 * �{�[�����W�̒��S�ɋÏk����Ă����悤�ȃG�t�F�N�g��������B<br>
	 * �܂��A�I�u�W�F�N�g�̃X�R�[�v�̊֌W����A"NEW BALL GENERATING"�̕����́A<br>
	 * �����N���X���Ń��[�h����d�l�Ƃ���
	 * */
	public void ballGenerationEffect(Ball newBall) {
		// TODO Auto-generated method stub

		/* #################
		 * �e��I�u�W�F�N�g�̏���
		 * #################*/

		//�`�悵�Ă��炤�����摜���悹�邽�߂̉摜�B
		//�X���b�h�̃��[�v���ƂɐV�K�쐬����̂ŁA���͂ЂƂ܂��^�����錾
		BufferedImage finalImage = null;

		//�Ïk�����~�̃G�t�F�N�g�Ɏg���A�~�`�̃f�[�^��p�ӁB
		//�~�̌��́A���16�Ƃ���B
		//�����Œ�Ȃ̂�List�͎g���Ă��Ȃ����A
		//�A���S���Y���́AenemyClass��BlastImageThread���Q�l�ɂ��Ă���

		//�~�̒��S���W�B(32, 32)����̋�����24px�ȓ��ł�����W�������_���w�肳���
		final Point[] circleCenters = new Point[15];
		//�~�ɂ�����ړ����̃x�N�g���B�~�̒��S���W����v�Z�������̂Ƃ���B
		//�X�J���[�͈��l�����A�x�N�g����(x,y)�������ϐ��������Ă���̂ł�����K�v
		final EntityVector[] circleVectors = new EntityVector[15];
		//�~�̔��a�B�ō�8�ŁA(32, 32)�̍��W�ɋ߂��Ȃ�΂Ȃ�قǏ������Ȃ�
		final Integer[] circleRadiuses = new Integer[15];

		//circleCenters������܂�΁A�c���͎����I�Ɋm�肷��B
		//���̐�̖����N���X���ł��������g���񂵂����̂ŁA
		//����V�[�N�G���X�͕ʃ��\�b�h�ɐ؂�o���B
		//���S���W�̏���ƁA����ɔ����x�N�g���Ɖ~���a�̌v�Z���s��
		for(int i = 0; i < circleCenters.length; i++) {
			circleCenters[i] = initializeCircleCenter();
			circleVectors[i] = calculateCircleVector(circleCenters[i]);
			circleRadiuses[i] = calculateCircleRadius(circleCenters[i]);
		}

		/* ######################
		 * effectImageList�ւ̓o�^
		 * ######################*/

		//�n���ꂽnewBall�̍��W����A�`��̈�̍�����W���v�Z
		int imageX = newBall.getCenterLocation().x - 32;
		int imageY = newBall.getCenterLocation().y - 32;

		//effectImageList�ɁA�C���[�W��o�^
		final EffectImageProperty myImageProperty
		= new EffectImageProperty(finalImage, imageX, imageY, 1);
		effectImageList.add(myImageProperty);

		/* ####################
		 * �����N���X�̃X���b�h�𔭍s
		 * ####################*/

		//�X���b�h�𔭍s�B�Ȃ��A���̓����ō��ꂽmyImage���A�Ō��finalImage�ɔ��f
		Thread thread = new Thread(){
			int effectTime = 0;//�o�ߎ��Ԃ̃J�E���g
			BufferedImage myImage = null;
			Graphics g;
			BufferedImage newBallGenerating = null;
			boolean displaysMessage = true;//���̃t���[���ŕ�����\�������邩�ǂ���
			final int blinkSpan = 200;//�����̓_�Ŏ��(ms)
			int ellapsedSpan = 0;//�����̖��ł�؂�ւ��Ă���o�߂�������(ms)


			@Override
			public void run(){
				//�܂��͍ŏI�C���[�W��Null��
				myImage = null;

				//���[�v�ɓ˓�钼�O�ŁA"NEW BALL GENERATING"�����[�h
				try {
					newBallGenerating
					= ImageIO.read(getClass().getResource("NewBallGenerating.bmp"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//���[�h�����������Ȃ�A���悢�惋�[�v�ɓ˓�
				while (effectTime <= 2000) {

					//effectImageList�ɓo�^�������g�̉摜������
					myImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
					g = myImage.getGraphics();
					/* ##################
					 * �Ïk����Ă����~�̕`��
					 * ##################*/

					for (int i = 0; i < circleCenters.length; i++) {
						//���炩���ߗp�ӂ��Ă������~�̃f�[�^�����ƂɁA�摜��`��B
						//��͂�~�͔����h���āA���ŉ����
						g.setColor(Color.WHITE);
						g.fillOval(	circleCenters[i].x - circleRadiuses[i],
									circleCenters[i].y - circleRadiuses[i],
									circleRadiuses[i] * 2,
									circleRadiuses[i] * 2);

						g.setColor(Color.BLACK);
						g.drawOval(	circleCenters[i].x - circleRadiuses[i],
									circleCenters[i].y - circleRadiuses[i],
									circleRadiuses[i] * 2,
									circleRadiuses[i] * 2);
					}

					/* #############
					 * �����摜�̕`��
					 * #############*/

					//�������݂�displaysMessage��true�Ȃ�A���̏�ɕ����C���[�W��
					//�`������
					if (displaysMessage == true) {
						g.drawImage(
								newBallGenerating,
								0, (64 - newBallGenerating.getHeight()) / 2,
								parentFrame
								);
					}

					/* #########
					 * �摜�̓��e
					 * #########*/

					myImageProperty.image = myImage;

					/* ###############
					 * �e��ϐ��̍Čv�Z
					 * ###############*/

					//���̃��[�v�ɔ����A���ꂼ��̒l���Čv�Z
					for (int i = 0; i < circleCenters.length; i ++) {
						//�~���S���ړ�
						circleCenters[i].x
						= circleCenters[i].x + circleVectors[i].getX();
						circleCenters[i].y
						= circleCenters[i].y + circleVectors[i].getY();

						//���̎��_�ŉ~���S���W��(31, 31)~(34, 34)�̂����ꂩ�Ȃ�A
						//�ēx�ʒu������
						if ( (circleCenters[i].x >= 31 && circleCenters[i].x <= 34) &&
								(circleCenters[i].y >= 31 && circleCenters[i].y <= 34)) {
							circleCenters[i] = initializeCircleCenter();
						}


						//�x�N�^�[���X�V
						circleVectors[i] = calculateCircleVector(circleCenters[i]);

						//���a���X�V
						circleRadiuses[i] = calculateCircleRadius(circleCenters[i]);
					}

					//�����_�ł̂��߂̃t���O����
					if (ellapsedSpan >= blinkSpan) {
						displaysMessage = ! displaysMessage;//�t���O�𔽓]
						ellapsedSpan = 0;//�o�ߎ��Ԃ̌v�Z��0�Ƀ��Z�b�g
					}

					//1�t���[���X���[�v
					try {
						Thread.sleep(16);
						effectTime = effectTime + 16;
						ellapsedSpan = ellapsedSpan + 16;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}//�������[�v�I��

			}//run���\�b�h�I��

		};//Thread�̖����N���X�I��

		//�X���b�h���X�^�[�g
		thread.start();

		//��L�̃X���b�h�̏������I���̂�҂�
		//(�������҂͍̂ő��2500ms�܂ŁB����ȏォ�������狭���I��
		//�ȉ��̍�Ƃ𑱍s)
		try {
			thread.join(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//�����A�j���[�V�������I�������u�Ԃɂ����āA
			//�Q�[���t�F�C�Y��ON_GAME���ێ��ł��Ă��Ȃ������Ȃ�A�{�[���͔j�󂳂��B
			//����if�߂�ʂ�ꍇ�Ƃ����̂́A�܂肱�̃{�[������������Ă���Œ���
			//�Q�[���I�[�o�[��������������A���̃{�[�����u���q�v�ɂȂ��Ă��܂����A
			//�Ƃ�����
			if (parentFrame.gamePhase != GamePhase.ON_GAME) {
				newBall.destructBall();
			}
			//�����{�[�����u���q�v�ɂȂ炸�ɍς񂾂Ȃ�A�ʏ�ʂ萶�����s����
			else {
				//�I������u�ԃ{�[���̃X���b�h���N��
				newBall.myTaskPerFrame.start();
				newBall.isBeingGenerated = false;
			}

			//�����̐����E���s�ɂ�����炸�A���g��effectImageList�ւ̓o�^�͕K���j��
			effectImageList.remove(myImageProperty);
		}



	}//ballGeneretionEffect�I��

		/**
		 * ballGenerationEffect���\�b�h�̃T�u���\�b�h�B<br>
		 * (32, 32)�̍��W����̋�����2px���傫��24px�ȉ��ł�����W�������_���ɕԂ��B<br>
		 * �u2px���傫���v�Ƃ����̂́A���W(32, 32)�ɂ��܂�ɋ߂��_���I�΂�Ă��܂��ƁA<br>
		 * �~���S�̈ړ��̍ہA�u���ٓ_�v�ł���(32, 32)�̍��W���~���S���˂������Ă��܂��A<br>
		 * �������`�悪����Ȃ��Ȃ�Ȃǂ̃o�O������������\�������z���Ă̂��Ƃł���B<br><br>
		 * �Ȃ��A�T�u���\�b�h�͒ʏ탁�\�b�h���C���f���g����i�[�����Ď��ʂ�����̂Ƃ���B<br>
		 * �܂��A���̏ꍇ�u�T�u���\�b�h�v�Ƃ́A<br>
		 * �u<b>����̃��\�b�h����؂�o���������̈ꕔ���ŁA<br>
		 * ����Č�����̃��\�b�h����Ăяo����邱�Ƃł����g���Ȃ����\�b�h</b>�v<br>
		 * �ƒ�`����B
		 * */
		private Point initializeCircleCenter(){

			//�����_�}�C�U���C���X�^���X��
			Random random = new Random();

			//���W���i�[����^��p��
			int x;
			int y;

			//�K�؂Ȍ��ʂ����܂ŁA�������J��Ԃ�
			while (true) {
				//x,y���W���A���ꂼ��8~55�̂������烉���_���ɑI�o
				x = random.nextInt(48) + 8;
				y = random.nextInt(48) + 8;

				//���̍��W�ƁA(32, 32)�Ƃ̋������O����̒藝�Ōv�Z
				double distance
				= Math.sqrt( Math.pow( (x - 32) , 2) + Math.pow( (y - 32) , 2) );

				//���̌v�Z���ʂ�2px���傫��24px�ȉ��Ȃ�A���̍��W���̗p
				if ( (distance <= 24) || (distance > 2) ) {
					break;
				}
			}

			//���̌v�Z���ʂ��A���W�Ƃ��ĕԋp
			return new Point(x, y);

		}

		/**
		 * ballGenerationEffect���\�b�h�̃T�u���\�b�h�B<br>
		 * ������W�����߂�ꂽ�~���A(32, 32)�Ɍ���߂ɕK�v�ȃx�N�g����Ԃ��B<br>
		 * �x�N�g���̃X�J���[�͉~��(32, 32)���W���牓���قǑ傫���Ȃ邪�A<br>
		 * Ball�N���X��BlastImage�ł̌o�������A�Œ�ł��X�J���[��2��ۏ؂��邱�ƁB<br><br>
		 * �Ȃ��A�T�u���\�b�h�͒ʏ탁�\�b�h���C���f���g����i�[�����Ď��ʂ�����̂Ƃ���B<br>
		 * �܂��A���̏ꍇ�u�T�u���\�b�h�v�Ƃ́A<br>
		 * �u<b>����̃��\�b�h����؂�o���������̈ꕔ���ŁA<br>
		 * ����Č�����̃��\�b�h����Ăяo����邱�Ƃł����g���Ȃ����\�b�h</b>�v<br>
		 * �ƒ�`����B
		 * */
		private EntityVector calculateCircleVector(Point circleCenter){
			//�܂��A�󂯎�������W���A(32, 32)����ǂꂭ�炢����Ă��邩���v�Z
			//���������W���W���X�g(32, 32)�������ꍇ�͂ǂ�����̂��H
			//��(32, 32)�ɂ��܂�ɋ߉߂�����W�́A���������I�΂��Ȃ��d�l�Ƃ���
			double distance
			= Math.sqrt( Math.pow( (circleCenter.x - 32) , 2)
						+ Math.pow( (circleCenter.y - 32) , 2) );

			//���̌v�Z���ʂɂ���āA�Œ�2~�ō�4�́A�X�J���[�ʂ��Z�o�B
			//�������傫����Α傫���قǁA�X�J���[���傫���Ȃ�
			int scholar = 2;//��L�̎d�l���A����l��2�Ƃ���
			if ( distance > 24 ) {
				scholar = 4;
			}else if ( distance < 12 ) {
				scholar = 2;
			}else{
				scholar = (int)distance / 6;
			}

			//�ȏ�̌v�Z���ʂƁA���S���W�̈ʒu�𓥂܂��āA�K�؂ȃx�N�g����Ԃ�
			EntityVector vector = new EntityVector(0, 0, 0);

			//�x�N�g���́A�ړI���W-���ݍ��W�ŋ��܂�
			vector.setX(32 - circleCenter.x);
			vector.setY(32 - circleCenter.y);

			//���̎��_��X������Y������32�Ȃ�A��O�����B
			//�����̒l��(1, 1)�ɃZ�b�g
			if ( (vector.getX() == 0) && (vector.getY() == 0) ) {
				vector.setX(1);
				vector.setY(1);
			}

			//�x�N�g�����߂���ŁA�X�J���[������
			vector.setScholar(scholar);

			//�ȏ�̌v�Z�ŏo���x�N�g���̃f�[�^��ԋp
			return vector;

		}

		/**
		 * ballGenerationEffect���\�b�h�̃T�u���\�b�h�B<br>
		 * ������W�����߂�ꂽ�~���A<br>
		 * ���݂̍��W�ɂ����Ăǂ�قǂ̔��a��L����ׂ������v�Z���ԋp����B<br>
		 * �~���S���W��(32, 32)�̍��W���牓����Ή����قǁA���a�͑傫���Ȃ�B<br><br>
		 * �Ȃ��A�T�u���\�b�h�͒ʏ탁�\�b�h���C���f���g����i�[�����Ď��ʂ�����̂Ƃ���B<br>
		 * �܂��A���̏ꍇ�u�T�u���\�b�h�v�Ƃ́A<br>
		 * �u<b>����̃��\�b�h����؂�o���������̈ꕔ���ŁA<br>
		 * ����Č�����̃��\�b�h����Ăяo����邱�Ƃł����g���Ȃ����\�b�h</b>�v<br>
		 * �ƒ�`����B
		 * */
		private int calculateCircleRadius(Point circleCenter){
			//���ꂪ�ԋp�l
			int radius = 0;

			//�܂��A�󂯎�������W���A(32, 32)����ǂꂭ�炢����Ă��邩���v�Z
			double distance
			= Math.sqrt( Math.pow( (circleCenter.x - 32) , 2)
						+ Math.pow( (circleCenter.y - 32) , 2) );

			//���̌v�Z���ʂɂ���āA1~8�̔��a���Z�o
			//�������傫����Α傫���قǁA�X�J���[���傫���Ȃ�
			if ( distance > 24 ) {
				radius = 8;
			}else if ( distance < 3 ) {
				radius = 1;
			}else{
				radius = (int)distance / 3;
			}

			//�ȏ�̌v�Z���ʂ�ԋp
			return radius;
		}

	/* ##########
	 * 3.�����N���X
	 * ##########*/

	/**
	 * EffectDrawer�N���X�̓����N���X�B�摜�Ƃ���x,y���W���i�[�B<br>
	 * �C���[�W�Ƃ���x,y���W���֘A�t���A�����I�ɉ^�p���邽�߂ɍ��ꂽ<br>
	 * ���b�p�[�N���X�ł�����
	 * */
	class EffectImageProperty{

		public BufferedImage image = null;
		public int x = 0;
		public int y = 0;
		public int magnification = 1;

		EffectImageProperty(BufferedImage imageInput, int xInput, int yInput, int magInput) {
			this.image = imageInput;
			this.x = xInput;
			this.y = yInput;
			this.magnification = magInput;
		}


	}



}
