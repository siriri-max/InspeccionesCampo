<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once 'database.php';
include_once 'Personas.php';

$db = new DataBase();
$instant = $db->getConnection();

$pinst = new Personas($instant);

$data = json_decode(file_get_contents("php://input"));


if(isset($data))
{

    $pinst->nombres = $data->nombres;
    $pinst->apellidos = $data->apellidos;
    $pinst->direccion = $data->direccion;
    $pinst->telefono = $data->telefono;
    $pinst->foto = $data->foto;

    if($pinst->createPerson())
    {
        http_response_code(200);
        echo json_encode( 
            array( "issuccess" => true,
            "message" => "Creado con exito"));
    }
    else
    {
        http_response_code(503); // Servicio no disponible
        echo json_encode( 
            array("issuccess" => false,
            "message" => "Error al crear"));
    }
}
else
{
    http_response_code(400);
    echo json_encode(array(
        "issuccess" => false,
        "message" => "Datos incompletos o inválidos"));

}



?>