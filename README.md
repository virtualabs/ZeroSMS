ZeroSMS
=======

ZeroSMS is a proof-of-concept demonstrating a way to send Class 0 SMS on android >=2.3.

/!\ This application only works on rooted phones.

How to install
--------------

Simply use adb to drop it onto your phone:

    adb remount
    adb push ZeroSms-signed.apk /system/app

And voila !

How to use it
-------------

Type in a phone number (no phonebook pickup, this is a PoC) and a text, then press "Send SMS !". Your SMS is sent.


Greetz
------

Vorex & Kaiyou
