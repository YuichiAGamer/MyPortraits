package com.cosmicbarrage;

import java.awt.Image;

/**
 * ���̃Q�[�����ŁA�摜�f�[�^�����N���X����������C�x���g���X�i�[�B
 * ���̃C�x���g���X�i�[�ł́A"DrawEventManager"�N���X��fireDrawEvent���N�������A
 * �e�N���X�̃C���X�^���X�����s����onDrawEvent���`���Ă���B
 * ���̒��ɁA�e�N���X�̃o�b�t�@�����O�C���[�W�ɉ��炩�̉摜��`�����ޏ������L�q���邱��*/
public interface DrawEventListener {

	void onDrawEvent(Image targetImage);
}
