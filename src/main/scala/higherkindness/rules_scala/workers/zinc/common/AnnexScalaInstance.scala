package higherkindness.rules_scala
package workers.zinc.common

import xsbti.compile.ScalaInstance

import java.io.File
import java.net.URLClassLoader
import java.util.Properties

final class AnnexScalaInstance(val allJars: Array[File]) extends ScalaInstance {

  override def version: String = actualVersion
  override lazy val actualVersion: String = {
    val stream = loader.getResourceAsStream("compiler.properties")
    try {
      val props = new Properties
      props.load(stream)
      props.getProperty("version.number")
    } finally stream.close()
  }

  override def compilerJar: File = null
  override lazy val libraryJar: File = allJars
    .find(f => new URLClassLoader(Array(f.toURI.toURL)).findResource("library.properties") != null)
    .get

  override def otherJars: Array[File] = null
  override lazy val loader: ClassLoader =
    new URLClassLoader(allJars.map(_.toURI.toURL), null)
  override def loaderLibraryOnly: ClassLoader = null
}
