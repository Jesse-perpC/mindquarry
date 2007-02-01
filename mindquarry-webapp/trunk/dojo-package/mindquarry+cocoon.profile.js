var dependencies = [ 
	"dojo.widget.*",
	"dojo.widget.Editor2",
	"dojo.widget.RichText",
	"dojo.widget.Editor2Toolbar",
	"dojo.widget.ColorPalette",
	"dojo.widget.Toolbar",
	"dojo.widget.Tooltip",
	"dojo.widget.Dialog",
	"dojo.widget.DropdownDatePicker",
	"dojo.widget.DropdownContainer",
	"dojo.io.*",
	"dojo.event.*",
	"dojo.lang.*",
	"dojo.AdapterRegistry",
	"cocoon",
	"cocoon.ajax.common",
	"cocoon.ajax.*",
	"cocoon.forms.*",
	"mindquarry.widget.AutoActiveButton",
	"mindquarry.widget.AutoActiveField",
	"mindquarry.widget.ChangePassword",
	"mindquarry.widget.IconSelect",
	"mindquarry.widget.MindquarryDatePicker",
	"mindquarry.widget.normalizeName",
	"mindquarry.widget.QuickSearch",
	"mindquarry.widget.SortableHTMLTable",
	"mindquarry.widget.TableExpandLink",
	"mindquarry.widget.TeamSwitcher",
	"mindquarry.widget.ToggleButton",
	"mindquarry.widget.wikiLink",
	"mindquarry.widget.TimerCFormAction"
];

dependencies.prefixes = [
   ["mindquarry", "../../mindquarry-webapp-resources/src/main/resources/COB-INF/scripts/dojo/mindquarry"],
   ["cocoon", "../cocoon"]
];

load("getDependencyList.js");
