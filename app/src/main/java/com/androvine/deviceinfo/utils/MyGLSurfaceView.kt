package com.androvine.deviceinfo.utils

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLSurfaceView : GLSurfaceView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var openGLInfoListener: OpenGLInfoListener? = null

    init {
        setEGLContextClientVersion(2) // Request an OpenGL ES 2.0 compatible context.
        setRenderer(MyRenderer())
    }

    fun setOpenGLInfoListener(listener: OpenGLInfoListener) {
        openGLInfoListener = listener
    }

    private inner class MyRenderer : Renderer {
        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            val vendor = GLES20.glGetString(GL10.GL_VENDOR)
            val renderer = GLES20.glGetString(GL10.GL_RENDERER)
            val version = GLES20.glGetString(GL10.GL_VERSION)
            val shaderVersion = GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION)
            val extensions = GLES20.glGetString(GL10.GL_EXTENSIONS)

            openGLInfoListener?.onOpenGLInfoReceived(vendor, renderer, version, shaderVersion, extensions)
        }

        override fun onDrawFrame(gl: GL10?) {
            // Not used in this example
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            // Not used in this example
        }
    }
}

interface OpenGLInfoListener {
    fun onOpenGLInfoReceived(
        vendor: String?,
        renderer: String?,
        version: String?,
        shaderVersion: String?,
        extensions: String?
    )
}