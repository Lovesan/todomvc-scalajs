package todomvc

import scala.scalajs.js.Dictionary
import org.scalajs.dom.window
import org.scalajs.jquery.jQuery
import org.scalajs.jquery.JQuery
import org.scalajs.jquery.JQueryEventObject
import todomvc.Model.SelectedFilter

object Controller {
    def getIdFromElt(elt: JQuery) : Int = {
        jQuery(elt.parents("li")).attr("data-id").toInt    
    }

    def handleCheck(elt: JQuery) : Unit = {
        var id = getIdFromElt(elt)
        Model.toggleItem(id)
        View.render()
    }

    def handleToggleAll() : Unit = {
        Model.toggleAll()
        View.render()
    }

    def handleAdd() : Unit = {
        var text = jQuery("#new-todo").value().asInstanceOf[String]
        if(text != "") {
            jQuery("#new-todo").value("")
            Model.addItem(text)
            View.render()
        }
    }

    def handleDelete(elt: JQuery) : Unit = {
        var id = getIdFromElt(elt)
        Model.removeItem(id)
        View.render()
    }

    def handleDeleteCompleted() : Unit = {
        Model.removeCompleted()
        View.render()    
    }

    def handleStartEditing(elt: JQuery) : Unit = {
        var li = jQuery(elt.parents("li").apply(0))
        var input = li.find(".edit")
        jQuery(".editing").removeClass("editing")
        li.addClass("editing")
        input.focus()
    }

    def handleEndEditing(elt: JQuery) : Unit = {
        var li = jQuery(elt.parents("li").apply(0))
        var input = li.find(".edit")
        var id = getIdFromElt(elt)
        var text = input.value().toString()
        li.removeClass("editing")
        if(text != "") {
           Model.updateItem(id, text)
        } else {
           Model.removeItem(id)
        }
        View.render()
    }

    def handleAbortEditing(elt: JQuery) : Unit = {
        View.render()
    }

    def handleHashChange() : Unit = {
        var hash = window.location.hash
        Model.filter = hash match {
            case "#/active" => SelectedFilter.Active
            case "#/completed" => SelectedFilter.Completed
            case _ => SelectedFilter.All
        }
        View.render()
    }

    def setupUI() : Unit = {
        jQuery("#new-todo").keyup((evt: JQueryEventObject) => {
            var dict = evt.asInstanceOf[Dictionary[Int]]
            var keyCode = dict.apply("keyCode")
            if(keyCode == 13){
                handleAdd()
            }
        })
        jQuery("#todo-list").on("click", "input[class=toggle]", (evt: JQueryEventObject) => {
            handleCheck(jQuery(evt.target))
        })
        jQuery("#todo-list").on("click", ".destroy", (evt: JQueryEventObject) => {
            handleDelete(jQuery(evt.target))
        })
        jQuery("#todo-list").on("dblclick", "label", (evt: JQueryEventObject) => {
            handleStartEditing(jQuery(evt.target))
        })
        jQuery("#todo-list").on("keyup", ".edit", (evt: JQueryEventObject) => {
            var dict = evt.asInstanceOf[Dictionary[Int]]
            var keyCode = dict.apply("keyCode")
            var target = jQuery(evt.target)
            if(keyCode == 13) {
                handleEndEditing(target)
            } else if(keyCode == 27) {
                handleAbortEditing(target)
            }
        })
        jQuery("#main").on("click", "#toggle-all", () => {
            handleToggleAll()
        })
        jQuery("#footer").on("click", "#clear-completed", () => {
            handleDeleteCompleted()
        })
        jQuery(window).bind("hashchange", () => {
            handleHashChange()
        })
        handleHashChange()
    }
}