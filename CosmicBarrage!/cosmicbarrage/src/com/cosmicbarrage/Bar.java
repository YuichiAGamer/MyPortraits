package com.cosmicbarrage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import com.cosmicbarrage.DrawEventManager.DrawingOrder;

/**
 * �ł��Ԃ��̂��߂̖_���i������N���X
 * */
public class Bar implements DrawEventListener{

	/**
	 * �t�B�[���h(�����o�ϐ�)
	 * */
	public int width = 128;
	public int height = 16;
	public int x = (640 - width) / 2;
	public int y =448;

	//Bar�̈ړ��X�s�[�h
	public int speed = 16;

	//�e�t�H�[���ւ̎Q��
	public CosmicBarrage parentFrame;

	//���ݕ��L�[��������Ă��邩�ǂ����̃t���O
	public boolean isKeyPressed = false;

	//���ݕ��L�[��������Ă�����B"Left"��"Right"��"Neutral"�����
	public String direction = "Neutral";

	//�N���X�O����ł��X���b�h���N���ł���悤�����o�ϐ��Ƃ���Thread�^��錾
	public Thread barMovingThread;

	//���݂���bar�����Ă��邩�ǂ����������t���O�B
	//���̃t���O��false�ł���Ԃ����Abar�͑�����󂯕t����
	public boolean isBroken = false;

	//�R���X�g���N�^
	Bar(CosmicBarrage parentFrameInput){
		//�e�N���X�̎Q�Ƃ��Q�b�g
		this.parentFrame = parentFrameInput;

		//�e�N���X�ւ̎Q�Ƃ��o�R���āA�C�x���g�}�l�[�W���Ɏ��g��o�^
		this.parentFrame.drawEventManager.addDrawEvent(this, DrawingOrder.MIDDLE);

		//bar�𓮂������߂̃X���b�h���쐬&�N�����A�������[�v
		barMovingThread = new Thread(){
			@Override
			public void run(){
				//�ȉ��̏����𖳌����[�v
				while(isBroken == false){
					if ((isKeyPressed == true) && (direction == "Left")){

						//�������[�łȂ����bar�����ɓ�����
						if (x > 0){
							x = x - speed;
						}

					}
					else if ((isKeyPressed == true) && (direction == "Right")){

						//�����E�[�łȂ����bar���E�ɓ�����
						if (x < 640 - width){
							x = x + speed;
						}

					}

					//�X���[�v
					try {
						sleep(16);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//�������[�v�I��


			}//run���\�b�h�I��

		};//Thread�̖����N���X�錾�I��
	}//�R���X�g���N�^�I��

	/**
	 * �Q�Ƃ�n���ꂽ�摜�ɑ΂��A���݂̃o�[�̍��W�ɁA�o�[�̉摜��`������
	 * */
	public void onDrawEvent(Image targetImage) {
		// TODO Auto-generated method stub
		Graphics g = targetImage.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(this.x, this.y, this.width, this.height);
		g.setColor(Color.BLACK);
		g.drawRect(this.x, this.y, this.width, this.height);
		g.dispose();
	}

	/* ###############
	 * 4.�f�X�g���N�^���\�b�h
	 * ###############*/

	/**
	 * ���̃C���X�^���X���g��j�󂷂郁�\�b�h�B<br>
	 * ��̓I�ɍs���鏈���͈ȉ��̒ʂ�B<br><br>
	 * 1.isBroken�t���O��true�ɃZ�b�g<br>
	 * 2.�e�N���X��bar�^����A���g�̓o�^��remove����<br>
	 * 3.�e�N���X��drawEventManager����A���g�̓o�^���O��<br>
	 * 4.�Ō��GC���蓮�N�����A���S�ɃC���X�^���X�����ł�����
	 * */
	public void destructBar() {

		//isBroken�t���O��true�ɃZ�b�g���A���g�̃X���b�h��������~
		isBroken = true;

		//�e�N���X��bar�^����A���g�̎Q�Ƃ�����
		parentFrame.bar = null;

		//�e�N���X��drawEventManager����A���g�̓o�^���O��
		parentFrame.drawEventManager.removeDrawEvent(this);

		//GC���蓮�N��
		System.gc();

	}

}//Bar�N���X�I��
