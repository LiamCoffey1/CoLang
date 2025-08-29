component button {
    view $<button>{buttonText}</button>$
}
component image {
    view $<image src = '{image}'></image>$
}
component card {
    view $<div class = "container"><c-image/><c-button/></div>$
}
fun getCards() {
    return $<div><c-card buttonText = "Go here" image = "google.ie"/><c-card buttonText = "Go here too" image =  "bing.ie"/></div>$
}
print $<div>{getCards()}</div>$