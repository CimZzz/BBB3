/*
 * Copyright (C) 2009 The Android Open Source Project *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at *
 *
 *
 *
 *
 *
 *
 *
 *
 */
#include <jni.h>
#include <string.h>
#include <stdlib.h>
#include <android/log.h>
#include "com_gogoh5_apps_quanmaomao_library_toolkits_EncryptUtils.h"

#define LOGI( ... ) __android_log_print( ANDROID_LOG_INFO, TAG, __VA_ARGS__ )


/* This is a trivial JNI example where we use a native method
 * * * * */

       void disturbKey( char *key )
       {
	       char	temp;
	       int	k = 255;
	       int	i, j;
	       for ( i = 0; i < 8; i++ )
	       {
		       j = (i * 3) % 8 + 8;
		       temp = key[i];
		       key[i] = key[j] ^ k;
		       key[j] = temp ^ k;
		       k = k >> 1;
	       }
       }


       void code( char* buffer, char* key, int length )
       {
	       int	iS[256];
	       char	iK[256];
	       int	i, j, x, t;
	       int	temp; char iY;
	       for ( i = 0; i < 256; i++ )
	       {
		       iS[i] = i;
	       }
	       for ( i = 0; i < 256; i++ )
	       {
		       iK[i] = key[i % 16];
	       }
/* the length of the key is 16. */

	       for ( i = 0, j = 0; i < 256; i++ )
	       {
		       j	= (j + iS[i] + (int) iK[i]) % 256; temp = iS[i];
		       iS[i]	= iS[j];
		       iS[j]	= temp;
	       }
	       i = 0; j = 0;
	       for ( x = 0; x < length; x++ )
	       {
		       i	= (i + 1) % 256; j = (j + iS[i]) % 256;
		       temp	= iS[i];
		       iS[i]	= iS[j];
		       iS[j]	= temp;
		       t	= (iS[i] + (iS[j] % 256) ) % 256; iY = (char) iS[t]; buffer[x] = buffer[x] ^ iY;
	       }
       }


       char* connectChars( char* a, int lena, char* b, int lenb )
       {
	       char* result = malloc( (lena + lenb + 1) * sizeof(char) ); int i, l = 0;
	       for ( i = 0; i < lena; i++ )
		       result[l++] = a[i];
	       for ( i = 0; i < lenb; i++ )
		       result[l++] = b[i];
	       result[lena + lenb] = '\0'; free( a );
	       free( b );
	       return result;
       }


       char* cutStringHeaderWith16Bytes( char* a, int length )
       {
	       if ( length < 17 )
		       return NULL;
	       char* result = malloc( (length - 15) * sizeof(char) ); int i;
	       for ( i = 0; i < length - 16; i++ )
	       {
		       result[i] = a[i + 16];
	       }
	       result[length - 16] = '\0';
	       free( a );
	       return (result);
       }


       char* jstringToChar( JNIEnv* env, jstring jstr )
       {
/* should free after use. */
	       char		* rtn		= NULL;
	       jclass		clsstring	= (*env)->FindClass( env, "java/lang/String" );
	       jstring		strencode	= (*env)->NewStringUTF( env, "utf-8" );
	       jmethodID	mid		= (*env)->GetMethodID( env, clsstring, "getBytes", "(Ljava/lang/String;)[B" ); jbyteArray barr = (jbyteArray) (*env)->CallObjectMethod( env, jstr, mid, strencode );
	       jsize		alen		= (*env)->GetArrayLength( env, barr );
	       jbyte		* ba		= (*env)->GetByteArrayElements( env, barr, JNI_FALSE );

	       if ( alen > 0 )
	       {
		       rtn = (char *) malloc( alen + 1 ); memcpy( rtn, ba, alen ); rtn[alen] = '\0';
	       }
	       (*env)->ReleaseByteArrayElements( env, barr, ba, 0 ); return rtn;
       }


       char* jbyteArrayToChar( JNIEnv* env, jbyteArray array, int* length )
       {
	       int len = (*env)->GetArrayLength( env, array );
	       *length = len;
	       char* result = malloc( (len + 1) * sizeof(char) ); (*env)->GetByteArrayRegion( env, array, 0, len, (jbyte *) result ); result[len] = '\0';
	       return result;
       }


       jbyteArray charToJbyteArray( JNIEnv* env, char* chars, int length )
       {
	       jbyteArray rtnArr = NULL;
	       rtnArr = (*env)->NewByteArray( env, length ); (*env)->SetByteArrayRegion( env, rtnArr, 0, length, (jbyte *) chars ); return rtnArr;
       }


/*
 * Class:     com_gogoh5_apps_quanmaomao_library_toolkits_EncryptUtils
 * Method:    encode
 * Signature: (Ljava/lang/String;[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_gogoh5_apps_quanmaomao_library_toolkits_EncryptUtils_encode
  (JNIEnv * env, jclass cls, jstring originalString, jbyteArray keyArray)
{
   char	* buffer = jstringToChar( env, originalString );
   int	length	 = strlen( buffer );
   int	keyLength;
   char	* key = jbyteArrayToChar( env, keyArray, &keyLength );
   char* encodedChar;
   code(buffer, key, length );
   disturbKey( key );
   encodedChar = connectChars( key, 16, buffer, length );
   jbyteArray result = charToJbyteArray( env, encodedChar, length + 16 );
    free( encodedChar );
   return result;
}


/*
 * Class:     com_gogoh5_apps_quanmaomao_library_toolkits_EncryptUtils
 * Method:    decode
 * Signature: ([B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_gogoh5_apps_quanmaomao_library_toolkits_EncryptUtils_decode
  (JNIEnv * env, jclass cls, jbyteArray originalBytes)
       {
	       int	i;
	       int	length;
	       char	* buffer = jbyteArrayToChar( env, originalBytes, &length );
	       char	* cutBuffer;
	       char	* key = (char *) malloc( 17 * sizeof(char) ); for ( i = 0; i < 16; i++ )
	       {
		       key[i] = buffer[i];
	       }
	       key[16] = '\0';
	       disturbKey( key );
	       cutBuffer = cutStringHeaderWith16Bytes( buffer, length );
	       code( cutBuffer, key, length - 16 );
	       jbyteArray result = charToJbyteArray( env, cutBuffer, length - 16 );
	       free( key );
	       free( cutBuffer );
	       return result;
       }


