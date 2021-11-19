
import 'dart:async';

import 'package:flutter/services.dart';

class ZendeskSupport {
  static const MethodChannel _channel = MethodChannel('zendesk_support');

  static void requestTicket() async {
    await _channel.invokeMethod('requestTicket');
  }
}
