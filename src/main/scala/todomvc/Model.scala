package todomvc

import scala.scalajs.js.Array

object Model {

    class Item(newId: Int, newText: String) {
        var id = newId
        var text = newText
        var completed = false

        def toggle() : Boolean = {
            completed = !completed  
            completed
        }

        override def toString() : String = {
            "#<" +
            id.toString() +
            ", '" +
            text +
            "', " +
            completed.toString() +
            ">"
        }
    }

    object SelectedFilter extends Enumeration {
        type SelectedFilter = Value
        val All, Active, Completed = Value
    }

    import SelectedFilter._

    var maxId: Int = 0
    var items: Array[Item] = new Array()
    var filter: SelectedFilter = All
    var toggled: Int = 0

    def allToggled() = {
        items.length > 0 && items.length == toggled
    }

    def findItem(id: Int) : Item = {
        var i = 0
        var len = items.length
        var found = false
        var item: Item = null
        while(i < len && !found) {
            item = items.apply(i)
            if(item.id == id){
              found = true
            }
            i += 1
        }
        item
    }

    def addItem(text: String) : Unit = {
        maxId = maxId + 1
        var item = new Item(maxId, text)
        items.push(item)
    }

    def removeItem(id: Int) : Unit = {
        var item = findItem(id)
        if(item != null) {
            if(item.completed) {
              toggled -= 1
            }
            items = items.filter((i: Item) => i.id != id)
        }
    }

    def updateItem(id: Int, text: String) : Unit = {
        var item = findItem(id)
        if(item != null) {
            item.text = text
        }
    }

    def removeCompleted() : Unit = {
        var ids = new Array[Int]()
        for(item <- items) {
            if(item.completed) {
              ids.push(item.id)
            }
        }
        for(id <- ids) {
            removeItem(id)
        }
    }

    def toggleItem(id: Int) : Unit = {
        var i = 0
        var item = findItem(id)
        if(item != null) {
            toggled += (if(item.toggle()) 1 else -1)
        }
    }

    def toggleAll() : Unit = {
        if(toggled != items.length) {
            for(item <- items) {
                if(!item.completed) {
                    toggled += 1
                }
                item.completed = true
            }
        } else {
            for(item <- items) {
                if(item.completed) {
                  toggled -= 1
                }
                item.completed = false
            }
        }    
    }
}