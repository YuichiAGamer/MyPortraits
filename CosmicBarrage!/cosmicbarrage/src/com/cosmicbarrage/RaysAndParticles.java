package com.cosmicbarrage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import com.cosmicbarrage.DrawEventManager.DrawingOrder;

/**
 * CosmicBarrage��ʂ̔w�i�ŁA��ʒ��S�����(Ray)�◱�q(Particle)�����ł���悤��
 * �G�t�F�N�g�������邽�߂̃N���X�B
 * ���̃N���X���C���X�^���X��������ƁA16��Flyer�I�u�W�F�N�g(�����N���X)����������A
 * ���ꂼ��1/2�̊m���Ń����_����Ray��Particle���������B
 * ����炪��ʒ�������g�U���邱�ƂŁA�G�t�F�N�g��������
 * */
public class RaysAndParticles implements DrawEventListener{

	//16��Flyer�N���X�̌^
	private Flyer[] flyer;
	//���q�𓮂������߂̃X���b�h�^�B�O������N���ł���悤public�錾
	public Thread flyerThread;
	//�e�N���X�ւ̎Q��
	public CosmicBarrage parentFrame;

	//�R���X�g���N�^
	RaysAndParticles(CosmicBarrage parentFrameInput){
		//�e�N���X�ւ̎Q�Ƃ��Q�b�g
		this.parentFrame = parentFrameInput;

		//���̎Q�Ƃ����ǂ��āA�e�N���X��drawEventListener�Ɏ������g��o�^
		parentFrame.drawEventManager.addDrawEvent(this, DrawingOrder.FIRST);

		//flyer������B�z��ɓ���I�u�W�F�N�g�́A��������K�v�A���H
		flyer = new Flyer[15];
		for(int i = 0; i < flyer.length; i++){
			flyer[i] = new Flyer();
		}

		//flyerThread�𖳖��N���X�Ƃ��ăC���X�^���X�����A����Ɏ��s
		flyerThread = new Thread(){
			@Override
			public void run(){

				while(true){

						moveFlyers();

					try {
						Thread.sleep(16);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//try�ߏI��
				}//�������[�v�I��

			}//run���\�b�h�I��
		};//Thread�̖����N���X�錾�I��

	}//�R���X�g���N�^�I��

	/*
	 * �ȉ��A���\�b�h����ѓ����N���X��񋓂���B
	 * �񋓂��鏇�Ԃ�
	 * 1.�C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h
	 * 2.�ʏ�̃��\�b�h
	 * 3.�����N���X
	 * �̏��Ƃ���B
	 * 1,2,3�̃O���[�v���ł́A���ꂼ��X��ABC���Ń����o������ł���B
	 * �I�[�o�[���[�h���ꂽ���\�b�h�́A�����Ȃ������瑽�����ɕ��ׂ�
	 * */

	/* ##############################
	 * 1.�C���^�[�t�F�C�X�ɂ��������ꂽ���\�b�h
	 * ##############################*/

	public void onDrawEvent(Image img) {
		// TODO Auto-generated method stub
		Graphics g = img.getGraphics();
		g.setColor(Color.BLACK);

		//�S�Ă̐�Ɨ��q�̕`��
		for(int i = 0; i < flyer.length; i ++){
			if (flyer[i].attribute == "Ray"){//��̕`��
				g.drawLine(flyer[i].x, flyer[i].y, flyer[i].xEnd, flyer[i].yEnd);
			}
			else{//���q�̕`��B��ʒ[�ɍs���قǁA���q���傫���Ȃ�
				int Diameter = calculateParticleDiameter(flyer[i].x, flyer[i].y);
				g.fillOval(flyer[i].x, flyer[i].y, Diameter, Diameter);
			}//if�ߏI��
		}//for�ߏI��

		g.dispose();
	}

	/* ############
	 * 2.�ʏ�̃��\�b�h
	 * ############*/

	/**
	 * drawRaysAndParticles�Ŏg���A���q�̒��a�����߂郁�\�b�h�B
	 * ���q�̌��݂�x,y���W����Ɏ��A��ʒ��S�̍��W���痣���قǖ߂�l�͑傫���Ȃ�
	 * */
	private int calculateParticleDiameter(int x, int y){
		int Diameter = 1;//���q���a
		double distance;//��ʒ��S����̋���
		double FrameLengthAverage;//��ʒ����̒��a����

		//�O����̒藝���g���āA�n���ꂽ���W�Ɖ�ʒ��S�̋����𑪒�
		distance = Math.pow((parentFrame.getWidth() / 2 - x), 2)
					+ Math.pow((parentFrame.getHeight() / 2 - y), 2);
		distance = Math.sqrt(distance);

		//���݂̃t���[���̑傫�����A���ƍ����̒��a���ς���Z�o�B
		//����Ȃ�t���[���̑傫�������ɑ傫���ϓ����Ă��A��r�I�Ó��ȕ��ϒ������Z�o�ł���
		FrameLengthAverage = Math.sqrt(parentFrame.getX() * parentFrame.getY());

		if (distance < FrameLengthAverage / 3){
			Diameter = 1;
		}else if (distance < FrameLengthAverage / 2){
			Diameter = 2;
		}else if (distance < FrameLengthAverage / 1){
			Diameter = 4;
		}else{
			Diameter = 8;
		}

		return Diameter;
	}

	/**
	 * ���̃N���X�̎���flyer�C���X�^���X���A���ꂼ��̎���vector�����Ԃ񂾂��A x,y���ɓ��������\�b�h�B
	 * ������g����x,y���W�������邱�Ƃɂ��A ��ʒ��������◱�q����o����鉉�o���s���B
	 * ��ʊO�ɂ͂ݏo���C���X�^���X�́u�Ăі߂��v���A���̒��ōs���B
	 * */
	public void moveFlyers(){

		for (int i = 0; i < flyer.length; i++){
			//���ۂ̈ړ����J�n����O�ɁA�������̎��_��flyer����ʊO��
			//�͂ݏo�Ă�����Aflyer�������ĉ�ʒ����Ɂu�Ăі߁v��
			if (flyer[i].x < 0 || flyer[i].y < 0
				|| flyer[i].x > parentFrame.getWidth()
				|| flyer[i].y > parentFrame.getHeight()){
					flyer[i].initializeProperty();
			}

			//�O�������s������A���W�̈ړ����J�n
			flyer[i].x = flyer[i].x + flyer[i].vector.getX();
			flyer[i].y = flyer[i].y + flyer[i].vector.getY();

			//flyer��attribute��Ray�Ȃ�AxEnd��yEnd�ɂ������v�Z���s��
			if (flyer[i].attribute == "Ray"){
				flyer[i].xEnd = flyer[i].xEnd + flyer[i].vector.getX();
				flyer[i].yEnd = flyer[i].yEnd + flyer[i].vector.getY();
			}

		}//for�ߏI��

	}//moveFlyers�I��

	/* ##########
	 * 3.�����N���X
	 * ##########*/

	/**
	 * ��◱�q�̏����i�[���Ă������߂̓����N���X�B
	 * ���̃N���X�̎�������x���W�Ay���W�A�O���̐�̃x�N�g���A
	 * ������"Ray"��"Particle"���ǂ��炩�����
	 * �����̕�����BRay����������Flyer�́A�X�ɃI�v�V�����̃����o�ϐ��Ƃ��āA
	 * ��̒����Ɛ�̏I���[��x,y���������B
	 * */
	class Flyer{

		int x;
		int y;
		EntityVector vector;//����������������֗��Ȃ̂ł������ł��g���B�����A���C���͂����x,y
		String attribute;
		int xEnd;
		int yEnd;

		/*�R���X�g���N�^�B������Flyer����ʊO�ɍs�����Ƃ��AFlyer���ēx������
		�������g���񂷗\��Ȃ̂ŁA������initializeProperty���\�b�h���N�����A
		����������ď���Ƃ���*/
		Flyer(){

			initializeProperty();

		}

		/**
		 * ���̃I�u�W�F�N�g�̎��v���p�e�B���܂Ƃ߂ď����郁�\�b�h�B
		 * �C���X�^���X�����y�сA��ʊO��Flyer���͂ݏo���ۂ́u�Ăі߂��v�Ɏg��
		 * */
		public void initializeProperty(){
			//�����_�}�C�U���C���X�^���X��
			Random random = new Random();

			//���̃C���X�^���X�̑�����"Ray"��"Particle"�������߂�
			if (random.nextBoolean() == true){
				this.attribute = "Ray";
			}else{
				this.attribute = "Particle";
			}

			/*Flyer�̃x�N�g�������������_���Ɍ��肷��B
			����ɂ��A����Flyer����ʏ�̑扽�ی��ɂ��邩���m�肷��B
			�x�N�g�������̏㉺���l�́A��ʂ̕�or�����́}0.5�{�Ƃ���*/
			vector 	= new EntityVector(
					random.nextInt(parentFrame.getWidth()) - parentFrame.getWidth() / 2,
					random.nextInt(parentFrame.getHeight()) - parentFrame.getHeight() / 2
					);

			//�����ɁAvector�̐�Βl��F�X�������Ă݂悤
			vector.setX(vector.getX() / 10);
			vector.setY(vector.getY() / 10);
			//vector������(0,0)�ɂȂ����ۂ̃o�O���
			if (vector.getX() == 0 && vector.getY() == 0){
				vector.setX(1);
				vector.setY(1);
			}

			/*�����āA����Flyer���扽�ی��ɂ��邩�ŁA����ʒu��(320, 240),(321, 240),
			(320, 241),(321, 241)�̂����ꂩ�Ɍ���B
			(320=��ʂ̕��̔����A240=��ʂ̍����̔���)
			PC��ʏ�̏ی��Ɛ��w�̊֐��̏ی��ł́Ay���W���t�]���Ă��邱�Ƃɒ���*/

			//vector��(x, y)������(+, -)=���ی��ɋ���ꍇ
			if ( (vector.getX() >= 0) && (vector.getY() < 0) ){
				this.x = parentFrame.getWidth() / 2 + 1;
				this.y = parentFrame.getHeight() / 2;
			}
			//vector��(x, y)������(-, -)=���ی��ɋ���ꍇ
			else if ( (vector.getX() < 0) && (vector.getY() < 0) ){
				this.x = parentFrame.getWidth() / 2;
				this.y = parentFrame.getHeight() / 2;
			}
			//vector��(x, y)������(-, +)=��O�ی��ɋ���ꍇ
			else if ( (vector.getX() < 0) && (vector.getY() >= 0) ){
				this.x = parentFrame.getWidth() / 2;
				this.y = parentFrame.getHeight() / 2 + 1;
			}
			//vector��(x, y)������(+, +)=��l�ی��ɋ���ꍇ
			else{
				this.x = parentFrame.getWidth() / 2 + 1;
				this.y = parentFrame.getHeight() / 2 + 1;
			}

			/*����Ɍ��߂�ꂽ������Particle�Ȃ�A�ȍ~�̏����͕K�v�Ȃ��̂�
			���̎��_�ŋ������^�[��*/
			if (this.attribute == "Particle"){
				return;
			}

			/*����Flyer��Attribute��"Ray"�Ȃ�A��̏I�_�̍��W���v�Z����B
			 * 0~1�܂ł̃����_���W�����܂��I�сA�ȉ��̌v�Z���s���B
			 * xEnd = this.x + vector.getX * (�����_���W��) * (������␳���邨�D�݂̌W��)
			 * yEnd = this.y + vector.getY * (�����_���W��) * (������␳���邨�D�݂̌W��)
			 * �Ȃ��Ax,y�ɂ͓���̌W�����g��Ȃ��Ɛ�Ɛi�s����v���Ȃ��Ȃ�̂Œ���*/
			double RandomCoefficient = random.nextDouble();
			this.xEnd = this.x + (int)(vector.getX() * RandomCoefficient) * 10;
			this.yEnd = this.y + (int)(vector.getY() * RandomCoefficient) * 10;

		}//initializeProperty�I��

	}

}//RaysAndParticles�N���X�I��