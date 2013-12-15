package de.halfreal.test.robolectric;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.SdkConfig;
import org.robolectric.bytecode.Setup;
import org.robolectric.res.Fs;
import org.robolectric.res.FsFile;
import org.robolectric.util.ActivityController;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class RobolectricMavenTestRunner extends RobolectricTestRunner {

	public static class MavenAndroidManifest extends AndroidManifest {

		@SuppressWarnings("deprecation")
		public MavenAndroidManifest(FsFile baseDir) {
			super(baseDir);
		}

		@Override
		protected AndroidManifest createLibraryAndroidManifest(
				FsFile libraryBaseDir) {
			return new MavenAndroidManifest(libraryBaseDir);
		}

		/**
		 * thanks to square for their nice article
		 */
		@Override
		protected List<FsFile> findLibraries() {
			// Try unpack folder from maven.
			FsFile unpack = getBaseDir().join("target/unpack/apklibs");
			if (unpack.exists()) {
				FsFile[] libs = unpack.listFiles();
				if (libs != null) {
					return Arrays.asList(libs);
				}
			}
			return Collections.emptyList();
		}

	}

	public static <T extends Activity> ActivityController<T> startActivity(
			Class<T> activity) {
		ActivityController<T> activityController = Robolectric
				.buildActivity(activity).create().start().resume();
		return activityController;
	}

	public static <T extends FragmentActivity> ActivityController<T> startFragment(
			Fragment fragment, Class<T> activityClass) {
		ActivityController<T> activityController = Robolectric
				.buildActivity(activityClass).create().start().resume();
		T activity = activityController.get();

		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(fragment, null);
		fragmentTransaction.commit();
		fragmentManager.executePendingTransactions();

		return activityController;
	}

	public RobolectricMavenTestRunner(Class<?> arg0) throws InitializationError {
		super(arg0);
	}

	@Override
	protected AndroidManifest createAppManifest(FsFile manifestFile,
			FsFile resDir, FsFile assetsDir) {
		return new MavenAndroidManifest(Fs.newFile(new File(".")));
	}

	@Override
	protected ClassLoader createRobolectricClassLoader(Setup setup,
			SdkConfig sdkConfig) {
		return super.createRobolectricClassLoader(setup, sdkConfig);
	}

}
