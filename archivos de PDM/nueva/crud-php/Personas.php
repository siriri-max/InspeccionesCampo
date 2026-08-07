<?php
class Personas
{

    private $conexion;
    private $table = "personas";

    public $id;
    public $nombres;
    public $apellidos;
    public $direccion;
    public $telefono;
    public $foto;

    // Constructor de la clase personas
    public function __construct($db)
    {
        $this->conexion = $db;
    }


    // Create
    public function createPerson()
    {
        $consulta = "INSERT INTO 
                    " . $this->table . "
                    SET 
                    nombres = :nombres,
                    apellidos = :apellidos,
                    direccion = :direccion,
                    telefono = :telefono,
                    foto = :foto ";

        $comando = $this->conexion->prepare($consulta);

        // Sanitizacion
        $this->nombres = htmlspecialchars(strip_tags($this->nombres));
        $this->apellidos = htmlspecialchars(strip_tags($this->apellidos));
        $this->direccion = htmlspecialchars(strip_tags($this->direccion));
        $this->telefono = htmlspecialchars(strip_tags($this->telefono));
        $this->foto = htmlspecialchars(strip_tags($this->foto));

        // bind data
        $comando->bindParam(":nombres", $this->nombres);
        $comando->bindParam(":apellidos", $this->apellidos);
        $comando->bindParam(":direccion", $this->direccion);
        $comando->bindParam(":telefono", $this->telefono);
        $comando->bindParam(":foto", $this->foto);

        if($comando->execute())
        {
            return true;
        }
        return false;
    }

    // Read
     
    public function GetListPersons()
    {
        $consulta = "SELECT * FROM " . $this->table . "";
        $comando = $this->conexion->prepare($consulta);
        $comando->execute();

        return $comando;
    }

    // Update
    
    public function updatePerson()
    {
        $consulta = "UPDATE " . $this->table . " SET 
                        nombres = :nombres,
                        apellidos = :apellidos,
                        direccion = :direccion,
                        telefono = :telefono,
                        foto = :foto
                    WHERE id = :id";

        $comando = $this->conexion->prepare($consulta);

        // Sanitización
        $this->nombres = htmlspecialchars(strip_tags($this->nombres));
        $this->apellidos = htmlspecialchars(strip_tags($this->apellidos));
        $this->direccion = htmlspecialchars(strip_tags($this->direccion));
        $this->telefono = htmlspecialchars(strip_tags($this->telefono));
        $this->foto = htmlspecialchars(strip_tags($this->foto));
        $this->id = htmlspecialchars(strip_tags($this->id));

        // Binding de datos
        $comando->bindParam(':nombres', $this->nombres);
        $comando->bindParam(':apellidos', $this->apellidos);
        $comando->bindParam(':direccion', $this->direccion);
        $comando->bindParam(':telefono', $this->telefono);
        $comando->bindParam(':foto', $this->foto);
        $comando->bindParam(':id', $this->id);

       if($comando->execute())
        {
            return true;
        }
        return false;
    }


    // Delete

    public function deletePerson()
    {
        $consulta = "DELETE FROM " . $this->table . " WHERE id = :id";

        $comando = $this->conexion->prepare($consulta);

        // Sanitización
        $this->id = htmlspecialchars(strip_tags($this->id));

        // Binding
        $comando->bindParam(':id', $this->id);

         if($comando->execute())
        {
            return true;
        }
        return false;
    }

}


?>