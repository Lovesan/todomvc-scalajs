package todomvc


import org.scalajs.jquery.jQuery
import todomvc.Model.SelectedFilter
import todomvc.Model.SelectedFilter.SelectedFilter

object View {
    
    def renderItems() : Unit = {
        var html = ""
        for(item <- Model.items) {
            var shouldRender = Model.filter match {
                case SelectedFilter.Completed => item.completed
                case SelectedFilter.Active => !item.completed
                case _ => true
            }
            if(shouldRender) {
                html += getItemTemplate(item.id, item.text, item.completed)
            }
        }
        jQuery("#todo-list").html(html)
    }

    def renderFooter() : Unit = {
        var footer = jQuery("#footer")
        var tpl = getFooterTemplate(Model.toggled, Model.items.length - Model.toggled, Model.filter)
        footer.html(tpl)
    }

    def renderButton() : Unit = {    
        var btn = jQuery("#toggle-all")
        var tpl = getToggleButtonTemplate(Model.allToggled())
        btn.replaceWith(tpl)
    }

    def render() : Unit = {
        renderButton()
        renderItems()
        renderFooter()
    }

    def getToggleButtonTemplate(checked: Boolean) : String = {
        var checkedAttr = if(checked) "checked='checked'" else ""
        s"<input id='toggle-all' type='checkbox' $checkedAttr>"
    }

    def getItemTemplate(id: Int, text: String, completed: Boolean) : String = {
        var liClass = if (completed) "completed" else ""
        var cbAttr = if (completed) "checked='checked'" else ""
        s"<li class='$liClass' data-id='$id'>" +
        "<div class='view'>" + 
        s"<input class='toggle' type='checkbox' $cbAttr>" +
        s"<label>$text</label>" + 
        "<button class='destroy'></button>" + 
        "</div>" +
        s"<input class='edit' value='$text'>" + 
        "</li>"
    }

    def getFooterTemplate(completed: Int, left: Int, filter: SelectedFilter) : String = {
        var allClass = if (filter == SelectedFilter.All) "selected" else ""
        var activeClass = if (filter == SelectedFilter.Active) "selected" else ""
        var completedClass = if (filter == SelectedFilter.Completed) "selected" else ""
        var todoWord = if (left == 1) "item" else "items"
        var clearButton = s"<button id='clear-completed'>Clear completed ($completed)</button>"
        s"<span id='todo-count'><strong>$left</strong>" +
        s" $todoWord left</span>" +
        "<ul id='filters'>" +
        "<li>" +
        s"<a class='$allClass' href='#/all'>All</a>" +
        "</li>" +
        "<li>" +
        s"<a class='$activeClass' href='#/active'>Active</a>" +
        "</li>" +
        "<li>" +
        s"<a class='$completedClass' href='#/completed'>Completed</a>" +
        "</li>" +
        "</ul>" +
        (if (completed > 0) clearButton else "")
    }
}