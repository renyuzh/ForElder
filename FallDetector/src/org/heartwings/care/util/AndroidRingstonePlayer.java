package org.heartwings.care.util;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * @author Inno520
 *
 * 该类用于播放铃声提醒用户
 */
public class AndroidRingstonePlayer {
	Context context;
	private MediaPlayer mMediaPlayer;

	public AndroidRingstonePlayer(Context context) {
		this.context = context;
		// Uri alert = RingtoneManager
		// .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		// mMediaPlayer = new MediaPlayer();
		// mMediaPlayer.setDataSource(context, alert);
		// audioManager = (AudioManager) context
		// .getSystemService(Context.AUDIO_SERVICE);
		// mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		// mMediaPlayer.setLooping(true);
		// mMediaPlayer.prepare();
	}

	public void play() {
		try {
			Uri alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
				mMediaPlayer.setLooping(true);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stop() {
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
		}
	}
}
