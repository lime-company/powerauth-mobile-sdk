# Note on Cryptography and Mobile Runtime Security

While we implement the cryptographic core as an isolated component in C/C++ to have as much control over the runtime and memory, the mobile application that uses our SDK ultimately run inside a potentially insecure mobile operating system. As such, the technical integrators of our SDK should consider attacks based on the weaknesses of mobile runtime.

This chapter attempts to provide an explanation of the issues to allow easier risk considerations, and also offer possible solutions.

## Mobile OS Security

Any security-related code, such as cryptographic core, ultimately runs in a mobile operating system, such as iOS or Android. While Apple and Google do their best to build secure software, hackers always find a way to bypass the system security features. General availability of jailbreak/rooting is a living testament to this, as well as the rise of Android mobile malware.

When running in a vulnerable OS, apps can be manipulated by an attacker (for example via mobile malware, or techniques such as “trust-jacking”) that is either:

- armed with a rooting framework (and hence can penetrate through the sandboxing features of a mobile OS, even on devices that were not previously rooted by the user), or …
- merely misusing some of the commonly available system interfaces, such as an ability to install own keyboards or screen readers in the system.

![ Weaknesses in Mobile Runtime ](./images/runtime-01.png)

### Examples of Attacks

Hackers can use the OS weaknesses and system interfaces to bypass existing security measures implemented in the mobile app. While this attack does not directly target the cryptographic core, it draws any security-related code ineffective since signatures can no longer be trusted. In other words, attackers will likely not tamper with the cryptographic core directly but instead, they will try hijacking the surrounding application in order to call the intact cryptographic core from the application code.

An example of such attack may look like this:

1. Attacker roots/jailbreaks/failbreaks the device.
2. Attacker either
  a) connects the debugger to the running app (attack "at runtime"), or
  b) modifies the code of an application bundle by repackaging (attack "at rest")
3. Attacker records the PIN code entered by the user, by listening to events on the UI components via debugger or by logging the events using a modified code.
4. Attacker prepares a fake payment and calls the cryptographic core with the payment data and a captured PIN code.

Note that the attack may be even performed without root on some devices and operating systems, using some insufficient system design. For example:

1. Attacker can prepares an app with the Accessibility service support on Android and tricks the user into activating such a service.
2. Attacker can record the PIN code using the accessibility service in the case of an incorrect PIN code implementation.
3. Attacker can launch the app from the background:
  a) use the accessibility service to replay the PIN code
  b) navigate through the app using taps and other gestures
  c) type in the payment details
  d) replay the PIN code again and confirm payment

As a result, mobile apps with high-security requirements cannot rely on the OS security features. Instead, they need to protect themselves with advanced obfuscation, app integrity checks and proactive anti-tampering features.

## Attack Vectors

There are several attack vectors that should be taken into account:

- **Rooting / Jailbreaking** - These terms represent a modification of mobile OS that essentially removes any built-in security measures, such as application sandboxing or permission control. While this change is usually harmless on its own, it is a strong enabler for several subsequent attack vectors, such as:
    - **Debugger Connection** - By connecting the debugger to the running application, attacker gains control over any data visible in the application code (for example, can read labels and buttons titles, intercept touches, etc.) and can manipulate the application code execution (for example, modify outcomes of methods, call any methods, navigate the user interface, etc.)
    - **App Repackaging** - By bypassing the application sandbox, an attacker may change or replace the application on the device with a fraudulent app version. This may be either a completely different app, or an original app  with some additional malicious code that logs events to obtain data from the app, or changes the app behavior to execute undesired business logic.
    - **Framework Injection or Native Code Hooks** - The attacker does not have to modify the application code directly. Instead, the attacker can simply modify a system framework or a library and change the behavior of any application indirectly as a result. This allows an attacker to modify the behavior of any app on a device without knowing much about a particular app code or structure.
    - **Device Cloning** - By bypassing the application sandbox, the attacker can obtain any data stored inside the application sandbox, including some records inside the protected space (such as records inside the iOS Keychain that are not protected, for example, using a biometry). While this does not allow directly bypassing the cryptographic algorithms such as PowerAuth (the data stored in the sandbox are further encrypted using a PIN code or stored in Secure Enclave in the case of biometry), attacker may get access to some secondary data, such as "possession factor" related signing keys, identifiers, or other data cached by the application.
- **Accessibility Services** (Android) - By tricking users into enabling the Accessibility service, attacker is able to read anything that happens in the device screen, or even perform gestures and type in the text. This indeed presents a major problem to any application that needs to keep the user data secure. Information about accounts or transactions may leak through the accessibility services, as well as PIN codes or passwords in the case of an incorrect implementation. The fact that many well-known apps misuse the Accessibility service for some "acceptable" tasks does not help as well...
- **Malicious Keyboards** - Mobile operating systems allow installing own keyboard that may use a custom logic to handle the user input. This obviously presents some possible security issues, especially when handling sensitive data, such as passwords or PIN codes. To make the situation worse, custom keyboards can be used to perform an overlay attacks (the keyboard would occupy bigger than usual space to present a fake UI over the screen) on the Android operating systems.

Of course, the vectors above can be combined quite creatively (for example, device cloning with PIN code theft) to amplify the damage.

_Note: Rooting and jailbreaking are generally terms used for a complete replacement of a legitimate OS with an OS that removes all protective mechanisms. However, attacker does not need to do a full rooting or jailbreaking to gain access to some system permissions. Instead, the attacker cam merely misuse some system vulnerability to escalate the user permissions. In such case, the result is in principle the same as if the attacker performed a full jailbreak or rooting: a full control over the end user's system._

## Typical Remedies

Generally speaking, it is not simple to reliably fix the above-mentioned issues with a "naive" implementation. In case you implement, for example, a rooting detection in the code, the malware with root access can find that particular code and disarm it. You would be surprised how easy this usually is - in the case of root, all you need to do is look for strings such as `"/sbin/su"` or `"/su/bin/su"`.

As a result, a new category of solutions emerged to provide much less straight-forward and much more sophisticated solutions that combine code obfuscation, evasion techniques, integrity checking, proactive anti-tampering features and other protective measures. These solutions are called [Mobile App Shielding](https://wultra.com/mobile-app-shielding), or RASP (Runtime Application Self-Protection).

Unless you are looking for a strictly formal fix (meaning "making the pen-testers happy, while not really fixing the actual issue"), we strongly suggest you deploy such technology.