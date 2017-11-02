package tsec.cipher.symmetric.imports

import tsec.cipher.symmetric._
import tsec.cipher.common.padding.NoPadding

sealed abstract class Encryptor[A: SymmetricCipher] {
  lazy val instance: Either[NoSuchInstanceError, JCASymmetricCipher[A, CTR, NoPadding]] =
    JCASymmetricCipher[A, CTR, NoPadding]

  @inline
  def keyGen(
      implicit keyGenerator: CipherKeyGen[A]
  ): CipherKeyGen[A] = keyGenerator

  def fromSingleArray(bytes: Array[Byte]): Either[CipherTextError, CipherText[A, CTR, NoPadding]] =
    CipherText.fromSingleArray[A, CTR, NoPadding](bytes)
}

object Encryptor {
  implicit val defaultEncryptor: Encryptor[AES128] = DefaultEncryptor
  implicit val mediumEncryptor: Encryptor[AES192] = MediumEncryptor
  implicit val strongEncryptor: Encryptor[AES256]  = StrongEncryptor
}

object DefaultEncryptor extends Encryptor[AES128]

object MediumEncryptor extends Encryptor[AES192]

object StrongEncryptor extends Encryptor[AES256]