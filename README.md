# YourAppIdea

[![Build Status](https://travis-ci.org/Michenux/YourAppIdea.svg?branch=master)](https://travis-ci.org/Michenux/YourAppIdea)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)

Project template for Android and demo app for tutorials on [michenux.net](http://www.michenux.net)

<a href='https://play.google.com/store/apps/details?id=org.michenux.yourappidea'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width='200'/></a>

## Requirements

* Android SDK 24 (with extras/Google Repository)
* Android Studio 2.2 (or later)

## What you can find inside

#### Implemented UI Patterns

* Material Design
* Navigation Drawer, NavigationView, CoordinatorLayout, AppBarLayout, ToolBar (AppCompatActivity)
* List/Detail (fragment)
* CardView
* Pull To Refresh
* Spinner in ToolBar
* Contextual toasts with SnackBar

#### Improved UX

* Fragment transition
* Different layouts in landscape mode
* No splash screen (because anti-pattern)

#### Architecture

* Dependency injection with Dagger
* RxJava for asking permissions and network requests

#### Persistence

* SQLite Database: manage creation/upgrade of database using SQL scripts
* ContentProvider examples
* AsyncTaskLoader and CursorLoader

#### Network

* JSON Request with Retrofit and OkHttp
* SyncAdapter
* Retrieve posts from WordPress website

#### Geoloc

* Localization with Google Play Services
* Geocoder example, Distance compute, sort by distance

#### Promote your app

* Link to the application on Google Play Store for adding rating/comments
* ChangeLog screen (can show what's new since previous version or display full changelog)

#### Monetize your app

* Integration of AdMob
* Donation screen (PayPal)

#### Others

* User sign in (Facebook, Google)
* PreferencesFragmentCompat
* EULA (End User Licence Agreement): accept/refuse EULA on first time or just display
* Multidex enabled

## Credits

Author: Laurent Michenaud (lmichenaud@gmail.com)

[![Follow me on Google+](http://www.michenux.net/images/g+64.png)](https://plus.google.com/+LaurentMichenaud/posts)
[![Follow me on Twitter](http://www.michenux.net/images/twitter64.png)](https://twitter.com/Michenux)
[![Follow me on LinkedIn](http://www.michenux.net/images/linkedin.png)](http://www.linkedin.com/pub/laurent-michenaud/5/148/b32)

# License

    Copyright 2013-2016 Laurent Michenaud

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
