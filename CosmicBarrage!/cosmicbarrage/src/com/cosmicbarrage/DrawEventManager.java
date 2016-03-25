package com.cosmicbarrage;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * ���̃p�b�P�[�W���̃N���X�ɂ�����DrawEvent�𓝊�����N���X�B
 * ���̒���List�ɁA�e�N���X�Ɏ������ꂽDrawEventListener��onDrawEvent��o�^���A
 * �����Ǘ�����B
 * */
public class DrawEventManager {

	//�o�^���ꂽ�C�x���g�̃��X�g
	private List<DrawEventListenerWithOrder> eventList = null;
	
	/**
	 * �R���X�g���N�^
	 * */
	public DrawEventManager(){
		eventList = new ArrayList<DrawEventListenerWithOrder>();
	}
	
	
	/**
	 * �w�肳�ꂽDrawEventListener���A�w�肳�ꂽDrawingOrder�Œǉ����郁�\�b�h�B
	 * �Ȃ��ADrawingOrder��錾���Ȃ��ꍇ�A
	 * �I�[�o�[���C�h���ꂽ���\�b�h�ɂ��AMIDDLE�񋓎q�������I�������
	 * */
	public void addDrawEvent(DrawEventListener namedEvent){
		//�񋓎q�̎����I��
		DrawingOrder drawingOrder;
		drawingOrder = DrawingOrder.MIDDLE;
		
		DrawEventListenerWithOrder wrappedEvent
		= new DrawEventListenerWithOrder(namedEvent, drawingOrder);
		eventList.add(wrappedEvent);
	}
	/**
	 * �w�肳�ꂽDrawEventListener���A�w�肳�ꂽDrawingOrder�Œǉ����郁�\�b�h�B
	 * �Ȃ��ADrawingOrder��錾���Ȃ��ꍇ�A
	 * �I�[�o�[���C�h���ꂽ���\�b�h�ɂ��AMIDDLE�񋓎q�������I�������
	 * */
	public void addDrawEvent(DrawEventListener namedEvent, DrawingOrder drawingOrder){
		DrawEventListenerWithOrder wrappedEvent
		= new DrawEventListenerWithOrder(namedEvent, drawingOrder);
		eventList.add(wrappedEvent);
	}
	
	/**
	 * �w�肳�ꂽDrawEventListener���A���̃I�[�_�[���Ƃ��̃N���X�̃��X�g����폜���郁�\�b�h
	 * */
	void removeDrawEvent(DrawEventListener namedEvent){
		//eventList��DrawEventListener�����b�v�����C���X�^���X�Ȃ̂ŁA
		//get���\�b�h��for�߂̃R���{�ő�����������āA�w�肳�ꂽ���O�̃��X�i�[��{������
		for (int i = 0; i < eventList.size(); i ++){
			if (eventList.get(i).event.equals(namedEvent) == true) {
				eventList.remove(i);
			}//if�ߏI��		
			
		}//for�ߏI��		
		
	}//removeDrawEvent���\�b�h�I��
	
	/**
	 * �����_�ł��̃N���X�ɓo�^���ꂽDrawEventListener�̃C�x���g����ċN�������郁�\�b�h�B
	 * �Ȃ��A�C�x���g���N������ہA�C�x���g��3�i�K�ɕ����čs����B
	 * �ŏ���DrawingOrder=FIRST�ł���C�x���g�A����MIDDLE�ł���C�x���g�A
	 * �����čŌ��LAST�ł���C�x���g�c�c�ƁA�����s���Ă���
	 * */
	public void fireDrawEvent(Image targetImage){
		//����eventList��null�Ȃ�A���̎��_�ŋ������^�[��
		if (eventList == null){
			return;
		}
		
		//�������^�[����������Ȃ��Ȃ�Afor�߂��g���āA���ݓo�^����Ă���C�x���g�������N��
		
		//�܂���FIRST�̃C�x���g
		for (int i = 0; i < eventList.size(); i++){
			if (eventList.get(i).order == DrawingOrder.FIRST){
				eventList.get(i).event.onDrawEvent(targetImage);
			}			
		}//FIRST��for�ߏI��
		
		//����MIDDLE�̃C�x���g
		for (int i = 0; i < eventList.size(); i++){
			if (eventList.get(i).order == DrawingOrder.MIDDLE){
				eventList.get(i).event.onDrawEvent(targetImage);
			}			
		}//MIDDLE��for�ߏI��
		
		//�Ō��LAST�̃C�x���g
		for (int i = 0; i < eventList.size(); i++){
			if (eventList.get(i).order == DrawingOrder.LAST){
				eventList.get(i).event.onDrawEvent(targetImage);
			}			
		}//LAST��for�ߏI��	
	}//fireDrawEvent���\�b�h�I��
	
	/**
	 * DrawEventListener�ɁADrawingOrder�̏����O������ǉ����A�^�p����ړI�ō쐬���ꂽ�����N���X�B
	 * DrawEventManager�N���X���ō쐬����郊�X�g�́A�����ケ�̃N���X�̃C���X�^���X�̃��X�i�[�ł���
	 * */
	private class DrawEventListenerWithOrder{
		
		//�����o�ϐ�(�t�B�[���h)
		DrawEventListener event = null;
		DrawingOrder order = null;
		
		//�R���X�g���N�^
		public DrawEventListenerWithOrder(DrawEventListener namedEvent,
				DrawingOrder drawingOrder) {
			// TODO Auto-generated constructor stub
			event = namedEvent;
			order = drawingOrder;
		}
		
	}//�����N���X�I��
	
	/**
	 * DrawEventManager�ɂ����Ďg���񋓌^�B
	 * ���݃��X�g�ɓo�^����Ă���C���^�[�t�F�C�X�̃��\�b�h���A�ǂ̃^�C�~���O�Ŏ��s���邩��߂�
	 * */
	public enum DrawingOrder{
		/**�ŏ��ɏ���*/
		FIRST,
		/**FIRST�̏�����ɏ���*/
		MIDDLE,
		/**�Ō�ɏ���*/
		LAST		
	}//�񋓌^�I��
}