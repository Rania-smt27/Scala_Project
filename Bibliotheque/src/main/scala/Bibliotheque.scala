
import scala.io._
import java.io._


class Bibliotheque {
  var livres: Vector[Livre] = Vector()

  def ajouterLivre(livre: Livre): Unit = {
    livres= livres :+livre
    println(s"Le livre '${livre.titre}' par ${livre.auteur} a été ajouté à la bibliothèque avec succés.")
  }

  def ajouterDetailLivre(): Livre = {
    println("Veuillez entrer les détails du livre (titre, auteur, année de publication):")
    val titre = StdIn.readLine()
    val auteur = StdIn.readLine()
    try {
      val annee = StdIn.readInt()
      if (titre.isEmpty || auteur.isEmpty || annee <= 0) {
        throw new IllegalArgumentException("Les détails du livre sont invalides.")
      }
      val livre = new Livre(titre, auteur, annee)
      //ajouterLivre(livre)
      livres= livres :+livre
      println(s"Le livre '${livre.titre}' par ${livre.auteur} a été ajouté à la bibliothèque avec succès.")
      return livre
    } catch {
      case _: NumberFormatException =>
        throw new IllegalArgumentException("L'année doit être un entier valide.")
    }

  }


  def emprunterLivre(titreLivre: String): Unit = {
    val livreOption = livres.find(_.titre.toLowerCase == titreLivre.toLowerCase) //Nous permet de verifier si le titre correspond a un des titre des livres qu'on a

    livreOption match {
      case Some(livre) if !livre.estEmprunte =>
        livre.emprunter()
        println(s"Le livre '$titreLivre' a été emprunté.")
      case Some(livre) =>
        println(s"Le livre '$titreLivre' est déjà emprunté.")
      case None =>
        println(s"Le livre '$titreLivre' n'existe pas dans la bibliothèque.")
    }
  }

  def rendreLivre(titreLivre: String): Unit = {
    livres.find(_.titre.toLowerCase == titreLivre.toLowerCase) match { //Nous permet de verifier si le titre correspond a un des titre des livres qu'on a
      case Some(livre) =>
        if (livre.estEmprunte) {
          livre.rendre()
          println(s"Le livre '$titreLivre' est rendu avec succés.")
        } else {
          println(s"Le livre '$titreLivre' n'est pas emprunté.")
        }
      case None =>
        println(s"Le livre '$titreLivre' n'existe pas dans la bibliothèque.")
    }
  }


  def rechercherLivre(): Unit = {
    println("Choisissez le critère de recherche en entrant le numero correspondant  :")
    println("1: Titre")
    println("2: Auteur")

    val choix = StdIn.readLine()

    choix match {
      case "1" =>
        println("Entrez le titre du livre que vous recherchez :")
        val titreRecherche = StdIn.readLine()
        rechercherLivreParCritere("titre", titreRecherche)

      case "2" =>
        println("Entrez le nom de l'auteur que vous recherchez :")
        val auteurRecherche = StdIn.readLine()
        rechercherLivreParCritere("auteur", auteurRecherche)

      case _ =>
        println("Choix invalide.")
    }
  }

  private def rechercherLivreParCritere(critere: String, valeurRecherchee: String): Unit = {
    critere.toLowerCase() match {
      case "titre" =>
        val livresTrouves = livres.filter(_.titre.toLowerCase.contains(valeurRecherchee.toLowerCase))
        afficherResultatRecherche(livresTrouves)

      case "auteur" =>
        val livresTrouves = livres.filter(_.auteur.toLowerCase.contains(valeurRecherchee.toLowerCase))
        afficherResultatRecherche(livresTrouves)

      case _ =>
        println("Critère de recherche invalide. Utilisez 'titre' ou 'auteur'.")
    }
  }


  private def afficherResultatRecherche(livresTrouves: Vector[Livre]): Unit = {
    println("Résultats de la recherche :")
    if (livresTrouves.nonEmpty) {
      livresTrouves.foreach { livre =>
        livre.afficherLivre()
      }
    } else {
      println("Aucun livre trouvé pour la recherche.")
    }
  }


  def afficherTousLesLivres(): Unit = {
    if (livres.nonEmpty) {
      println("Liste de tous les livres dans la bibliothèque :")
      livres.foreach(livre => livre.afficherLivre())
    } else {
      println("La bibliothèque est vide. Aucun livre n'a été ajouté.")
    }
  }


  def sauvegarderDansFichier(nomFichier: String): Unit = {
    val writer = new PrintWriter(new File(nomFichier))
    try {
      livres.foreach { livre =>
        writer.write(s"${livre.titre},${livre.auteur},${livre.anneeDePublication},${livre.estEmprunte}\n")
      }
    } finally {
      writer.close()
    }
  }

  // Nouvelle méthode pour charger depuis un fichier
  def chargerDepuisFichier(nomFichier: String): Unit = {
    val bufferedSource = Source.fromFile(nomFichier)
    try {
      for (line <- bufferedSource.getLines) {
        val Array(titre, auteur, annee, estEmprunte) = line.split(",")
        val livre = new Livre(titre, auteur, annee.toInt)
        if (estEmprunte.toBoolean) {
          livre.emprunter()
        }
        ajouterLivre(livre) // Ajouter le livre à la bibliothèque
      }
    } finally {
      bufferedSource.close()
    }
  }









}
