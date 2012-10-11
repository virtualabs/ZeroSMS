ZeroSMS
=======

ZeroSMS is a proof of concept I developed to show how to create an Android system app able to send Class 0 SMS.

How to install it ?
-------------------

Pretty easy:

  adb shell mount -o rw,remount
  adb push ZeroSMS-signed.apk /system/app

And voila !

How to use it ?
---------------

Start the application, type in a phone number (no phonebook support) and a text to send, and press "Send SMS" button. Your Class 0 SMS will be send shortly.

Greetz
------

Vorex & Kaiyou