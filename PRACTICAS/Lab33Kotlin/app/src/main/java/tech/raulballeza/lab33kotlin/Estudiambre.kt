package tech.raulballeza.lab33kotlin

import android.os.Parcel
import android.os.Parcelable

object Estudiambre : Parcelable {
    var id: String? = null
        private set
    var name: String? = null
    private set
    var grade: String? = null
    private set

            // Constructor
            init(id: String?, name: String?, grade: String?) {
        this.id = id
        this.name = name
        this.grade = grade
    }

    override fun toString(): String {
        return " ID: " + id + " Nombre " + name + " Calif" + grade
    }

    // Metodos necesarios para garantizar el correcto funcionamiento del objeto al pasarse entre formularios
// Se deben hacer las adaptaciones si hay cambios en la estructura
// Parcelling part
    constructor(`in`: Parcel) {
        val data = arrayOfNulls<String>(3)
        `in`.readStringArray(data)
        id = data[0]
        name = data[1]
        grade = data[2]
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeStringArray(
            arrayOf(
                id,
                name,
                grade
            )
        )
    }

    companion object {
        val CREATOR: Parcelable.Creator<*> = object : Parcelable.Creator<Any?> {
            override fun createFromParcel(`in`: Parcel): Estudiambre? {
                return Estudiambre(`in`)
            }

            override fun newArray(size: Int): Array<Estudiambre?> {
                return arrayOfNulls(size)
            }
        }
    }
}