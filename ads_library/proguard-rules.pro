#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontpreverify
#-verbose
#-classobfuscationdictionary encrypt.txt
#-packageobfuscationdictionary encrypt.txt
#-obfuscationdictionary encrypt.txt
#
#-keep public class vidoedownloadersocial.fastdownloader.fbinstadownlaoder.models.** {
#  *;
#}
#
#
#
#-keepclassmembers class * implements android.os.Parcelable {
#    public static final android.os.Parcelable$Creator *;
#}
#-keep public class com.google.android.gms.ads.** {
#   public *;
#}
#
#-keep class com.ads.adsmodule.** { *;}
