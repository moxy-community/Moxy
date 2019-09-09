We've introduced you into Moxy - MVP framework for splitting your application into logic layers and life-cycle managing. Now it's time to reduce boilerplate code with Android Studio Templates.


<h4><b>Project structure</b></h4>
First we should fix the project structure:

<ul>
	<li> model</li>
	<li> presentation
		<ul>
		<li> presenter</li>
		<li> view</li>
	</ul></li>
 
 <li> ui
		<ul>
		<li> activity</li>
		<li> fragment</li>
	</ul></li>
</ul>

Presenter, view, activity and fragment packages consist of logic modules. These modules are often the application's sections (e.g. intro, offers, feed)
 ![Activity template](https://github.com/moxy-community/Moxy/raw/develop/moxy-templates/images/project_structure.jpg)

Our goal is to generate this set of classes using Android Studio Templates.

<h4><b>Template settings</b></h4>

Adding Moxy templates to Android Studio:
* Download templates for **Java** from [Github](https://github.com/moxy-community/Moxy/tree/develop/moxy-templates/Java) or directly [using link](https://drive.google.com/file/d/0B0bXlVHPiZVXY2FVQkNLc1lMbW8/view?usp=sharing)
* Or download templates for **Kotlin** from [Github](https://github.com/moxy-community/Moxy/tree/develop/moxy-templates/Kotlin)
* Copy files to ANDROID_STUDIO_DIR/plugins/android/lib/templates/activities
* Restart Android Studio

Add hot keys for templates quick access:
<ul>
	<li>Open settings->Keymap</li>	
	<li>Enter "Moxy" to the search box</li>	
	<li>Add a key combination (We recommend Alt + A for Moxy Activity and Alt + F for Moxy Fragment)</li>
</ul>

 ![Activity template](https://github.com/moxy-community/Moxy/raw/develop/moxy-templates/images/keymap.jpg)

<h4><b>Templates usage</b></h4>

* Click your **ROOT** package and then press Alt + A.
* In Activity Name box enter "MyFirstMoxyActivity"

Other fields will fill automatically

 ![Activity template](https://github.com/moxy-community/Moxy/raw/develop/moxy-templates/images/activity_template.jpg)

Finally type in the Package Name field your subpackage name (e.g. first) and press Finish.

<b>Attention</b> After your Android Studio upgrade all the custom templates could be deleted. In this case you'll have to import them again.
