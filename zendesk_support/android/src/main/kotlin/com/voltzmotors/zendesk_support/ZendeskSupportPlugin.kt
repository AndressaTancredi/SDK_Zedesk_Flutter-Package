package com.voltzmotors.zendesk_support

import android.content.Context
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import zendesk.core.Zendesk

import zendesk.support.Support

import zendesk.core.AnonymousIdentity
import zendesk.core.Identity
import zendesk.support.request.RequestActivity
import zendesk.support.CustomField

/** ZendeskSupportPlugin */
class ZendeskSupportPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  var currentActivity : Context? = null
  private lateinit var channel : MethodChannel

  var customFieldOne = CustomField(1900002776827, "text")
  var customFieldTwo = CustomField(1900002776847, "some_text")

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "zendesk_support")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "requestTicket") {
      RequestActivity.builder()
        .withCustomFields(listOf(
          customFieldOne,
          customFieldTwo,
        )
        )
        .show(currentActivity!!);
      result.success(1)
    } else result.notImplemented()
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    Zendesk.INSTANCE.init(
      binding.activity, "https://andressatestevoltz.zendesk.com",
      "37c8da844d48f79f166f3b28e225f86f6d6301a3b7bca012",
      "mobile_sdk_client_c28173556b5d2e0cb8e6");

    val identity: Identity = AnonymousIdentity()
    Zendesk.INSTANCE.setIdentity(identity)

    Support.INSTANCE.init(Zendesk.INSTANCE)

    currentActivity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {
    currentActivity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    currentActivity = binding.activity
  }

  override fun onDetachedFromActivity() {
    currentActivity = null
  }
}
