package dincyclopedia.ui

import dincyclopedia.model.LeveledMagicModifier

import calico.*
import calico.html.io.*
import calico.html.io.given
import cats.Show
import cats.effect.*
import cats.syntax.all.*
import fs2.*
import fs2.concurrent.Signal
import fs2.dom.*

def tableRow[A: Show](name: String, value: Option[A]) =
  value.map(v => tr(td(name), td(v.show)))

def tableRow[A: Show](name: String, value: A) = tr(td(name), td(value.show))

trait ToHtml[A] {
  extension (a: A) {
    def toHtmlResource(title: String): Resource[IO, HtmlDivElement[IO]]
  }
}

given ToHtml[LeveledMagicModifier] with {
  extension (m: LeveledMagicModifier) {
    def toHtmlResource(title: String) = div(
      h3(title),
      table(
        tableRow("Prefix", m.prefix),                      // : Boolean
        tableRow("Magic Requirement", m.magicRequirement), // : Option[String]
        tableRow("Item Type Requirement", m.itemTypeRequirement), // : Option[String]
        tableRow("Cursed", m.cursed),            // : Boolean
        tableRow("Ego", m.ego),                  // : Boolean
        tableRow("Spawn Chance", m.spawnChance), // : Double
        tableRow("Requirements Multiplier", m.requirementsMult),
        tableRow("Available At Max Level", m.availableAtMaxLevel),
        tableRow("Proc Skill", m.proc.map(_.skill)), // : Option[MagicModifier.Proc]
        tableRow("Proc Chance", m.proc.map(_.chance)), // : Option[MagicModifier.Proc]
        tableRow("Proc Level", m.proc.map(_.level)), // : Option[MagicModifier.Proc]
        children[(String, Double)]((statName, statValue) =>
          tableRow(statName, statValue)
        ) <-- Signal.constant(m.stats.toList),
      ),
    )
  }
}

/*
extension [E <: Entry](e: E) {
  inline def toHtmlResource(using
      inline eMirror: Mirror.ProductOf[E]
  ): Resource[IO, HtmlDivElement[IO]] = ???
}
 */
