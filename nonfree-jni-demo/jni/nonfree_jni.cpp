// A simple demo of JNI interface to implement SIFT detection for Android application using nonfree module in OpenCV4Android.
// Created by Guohui Wang 
// Email: robertwgh_at_gmail_com
// Data: 2/26/2014

#include <jni.h>
#include <android/log.h>

#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/features2d/features2d.hpp>
#include "zip.h"
#include "nonfree.hpp"


using namespace cv;
using namespace std;

#define  LOG_TAG    "nonfree_jni_demo"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

typedef unsigned char uchar;

int run_demo(const char *str);

extern "C" {
    JNIEXPORT void JNICALL Java_com_example_nonfreejnidemo_NonfreeJNILib_runDemo(JNIEnv * env, jobject obj, jstring str);
    JNIEXPORT void JNICALL Java_com_example_nonfreejnidemo_NonfreeJNILib_readFromAssetsLibzip(JNIEnv* env, jclass tis,jstring assetpath, jstring filename);
};

JNIEXPORT void JNICALL Java_com_example_nonfreejnidemo_NonfreeJNILib_runDemo(JNIEnv * env, jobject obj, jstring str)
{
	LOGI( "Start run_demo! \n");
	jboolean iscopy;
	const char *mpath = env->GetStringUTFChars(str, &iscopy);
	run_demo(mpath);
	LOGI( "End run_demo!\n");
}

/*******************************************************************************
 * Function Name  : java_com_fontlose_ReadAssets_readFromAssetsLibzip
 * Description    : 定义：public native void  readFromAssetsLibzip(String apkpath,String filename);
 * Input          : apkpath路径 filename 资源名
 * Output         : None
 * Return         : None
 *******************************************************************************/
JNIEXPORT void JNICALL Java_com_example_nonfreejnidemo_readFromAssetsLibzip(JNIEnv* env, jclass tis,jstring assetpath, jstring filename)
{
	LOGI("ReadAssets");
	int i = 0;
	jboolean iscopy;
	const char *mpath = env->GetStringUTFChars(assetpath, &iscopy);
	struct zip* apkArchive = zip_open(mpath, 0, NULL);
	env->ReleaseStringUTFChars(filename, mpath);

	struct zip_stat fstat;
	zip_stat_init(&fstat);

	int numFiles = zip_get_num_files(apkArchive);
	LOGI("File numFiles %i \n", numFiles);
	for (i = 0; i < numFiles; i++) {
		const char* name = zip_get_name(apkArchive, i, 0);

		if (name == NULL) {
			LOGE("Error reading zip file name at index %d : %s",i, zip_strerror(apkArchive));
			return;
		}

		zip_stat(apkArchive, name, 0, &fstat);
		//LOGI("File %d:%s Size1: %d Size2: %d", i, fstat.name, fstat.size, fstat.comp_size);
	}

	const char *fname = env->GetStringUTFChars(filename, &iscopy);
	struct zip_file* file = zip_fopen(apkArchive, fname, 0);

	if (!file) {
		LOGE("Error opening %s from APK", fname);
		return;
	}

	zip_stat(apkArchive, fname, 0, &fstat);
	env->ReleaseStringUTFChars(filename, fname);
	char *buffer = (char *) malloc(fstat.size + 1);
	buffer[fstat.size] = 0;
	int numBytesRead = zip_fread(file, buffer, fstat.size);

	LOGI(": %s\n", buffer);
	free(buffer);
	zip_fclose(file);
	zip_close(apkArchive);
}


int run_demo(const char *str)
{
	//cv::initModule_nonfree();
	//cout <<"initModule_nonfree() called" << endl;

	// Input and output image path.
	const char * imgInFile = str;
	const char * imgOutFile = "/sdcard/DCIM/img1_result.jpg";

	Mat image;
	image = imread(imgInFile, CV_LOAD_IMAGE_COLOR);
	if(! image.data )
	{
		LOGI("Could not open or find the image!\n");
		return -1;
	}

	vector<KeyPoint> keypoints;
	Mat descriptors;

	// Create a SIFT keypoint detector.
	SiftFeatureDetector detector;
	detector.detect(image, keypoints);
	LOGI("Detected %d keypoints\n", (int) keypoints.size());

	// Compute feature description.
	detector.compute(image,keypoints, descriptors);
	LOGI("Compute feature.\n");

	// Store description to "descriptors.des".
	FileStorage fs;
	fs.open("descriptors.des", FileStorage::WRITE);
	LOGI("Opened file to store the features.\n");
	fs << "descriptors" << descriptors;
	LOGI("Finished writing file.\n");
	fs.release();
	LOGI("Released file.\n");

	// Show keypoints in the output image.
	Mat outputImg;
	Scalar keypointColor = Scalar(255, 0, 0);
	drawKeypoints(image, keypoints, outputImg, keypointColor, DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	LOGI("Drew keypoints in output image file.\n");

#ifdef WIN32
	namedWindow("Output image", CV_WINDOW_AUTOSIZE );
	imshow("Output image", outputImg);
	waitKey(0);
#endif
	
	LOGI("Generate the output image.\n");
	imwrite(imgOutFile, outputImg);

	LOGI("Done.\n");
	return 0;
}
