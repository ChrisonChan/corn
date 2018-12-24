package sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundSystem {
	int fp;
	boolean musice = false, sounde = false,paused=true;
	AudioInputStream  grounded, play, pause, win, lose, rotate, move, music,pm,SelectedMusic;
	Clip groundeds, plays, pauses, wins, loses, rotates, moves, musics;
	Thread t;
	boolean worked = false;
	public SoundSystem() {
		try {
			grounded = AudioSystem.getAudioInputStream(new File("src/sound/grounded.wav").getAbsoluteFile());
			play = AudioSystem.getAudioInputStream(new File("src/sound/play.wav").getAbsoluteFile());
			pause = AudioSystem.getAudioInputStream(new File("src/sound/pause.wav").getAbsoluteFile());
			lose = AudioSystem.getAudioInputStream(new File("src/sound/lose.wav").getAbsoluteFile());
			rotate = AudioSystem.getAudioInputStream(new File("src/sound/rotate.wav").getAbsoluteFile());
			move = AudioSystem.getAudioInputStream(new File("src/sound/move.wav").getAbsoluteFile());
			music = AudioSystem.getAudioInputStream(new File("src/sound/music.wav").getAbsoluteFile());
			pm = AudioSystem.getAudioInputStream(new File("src/sound/mmusic.wav").getAbsoluteFile());
			musics = AudioSystem.getClip();
			SelectedMusic = pm;
			musics.open(SelectedMusic);

			groundeds = AudioSystem.getClip();
			groundeds.open(grounded);

			plays = AudioSystem.getClip();
			plays.open(play);

			pauses = AudioSystem.getClip();
			pauses.open(pause);

			loses = AudioSystem.getClip();
			loses.open(lose);

			rotates = AudioSystem.getClip();
			rotates.open(rotate);

			moves = AudioSystem.getClip();
			moves.open(move);
			worked = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isWorking() {
		return worked;
	}
	
	public void pause() {
		if (sounde) {
			plays(pauses);
		}
	}

	public void play() {
		if (sounde) {
			plays(plays);
		}
	}

	public void grounded() {
		if (sounde) {
			plays(groundeds);
		}
	}

	public void lose() {
		if (sounde) {
			plays(loses);
		}
	}

	public void rotate() {
		if (sounde) {
			plays(rotates);
		}
	}

	public void move() {
		if (sounde) {
			plays(moves);
		}
	}

	public void setSounde(boolean b) {
		sounde = b;
	}

	public void setMusice(boolean b) {
		musice = b;
			if(b) {
				musics.setFramePosition(fp);
				musics.loop(Clip.LOOP_CONTINUOUSLY);
			}else {
				fp = musics.getFramePosition();
				musics.stop();
			}
	}


	public void plays(Clip clip) {
		if(clip!=null
				&&!(clip==moves&&musics.isRunning())
				) {
			clip.setFramePosition(0);
			clip.start();
		}
	}
	
	public void setPauseMusic() {
		paused = true;
		musics.close();
		try {
			musics = AudioSystem.getClip();
			pm = AudioSystem.getAudioInputStream(new File("src/sound/mmusic.wav").getAbsoluteFile());
			musics.open(pm);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		if(musice) {
			musics.start();
		}
	}
	public void setPlayMusic() {
		paused = false;
		musics.close();
		try {
			musics = AudioSystem.getClip();
			music = AudioSystem.getAudioInputStream(new File("src/sound/music.wav").getAbsoluteFile());
			musics.open(music);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		if(musice) {
			musics.start();
		}
	}
	
}
