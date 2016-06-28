<?php

class Enum {
  private static $values = array();
  private $name;
  private $ordinal;

  public static function __callStatic($name, $arguments) {
    return static::valueOf($name);
  }

  protected static function rawValues() {
    return array();
  }

	protected static function className() {
		return __CLASS__."";
	}

  protected static function build($args) {
    return null;
  }

  public static function values() {
    self::lazyLoadValues();
    return array_values(self::$values);
  }

  public static function names() {
    self::lazyLoadValues();
    return array_keys(self::$values);
  }

  private static function lazyLoadValues() {
    $rawValues = static::rawValues();
    $rawKeys = array_keys($rawValues);
    $last = end($rawKeys);
    static::valueOf($last);
  }

  public static function valueOf(string $name) {
    if (!array_key_exists($name, self::$values)) {
      $className = static::className();
      $rawValues = static::rawValues();
      if (!array_key_exists($name, $rawValues)) {
        throw new Exception($name." is not a valid value of ".$className);
      }
      $rawKeys = array_keys($rawValues);
      $keyIndex = array_search($name, $rawKeys);
      $valueCount = count($values);
      for ($x = $valueCount; $x <= $keyIndex; $x++) {
        $rawKey = $rawKeys[$x];
        $rawValue = $rawValues[$rawKey];
        $value = static::build($rawValue);
        $value->name = $rawKey;
        $value->ordinal = $x;
        self::$values[$value->name] = $value;
      }
    }
    return self::$values[$name];
  }

  public function name() {
    return $this->name;
  }

  public function ordinal() {
    return $this->ordinal;
  }
}

?>
