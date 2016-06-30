<?php

class TemplateEngine {
  static function apply(string $template, $vars) {
    $applied_template = $template;
    if (preg_match_all("/{{(.*?)}}/", $template, $m)) {
      foreach ($m[1] as $i => $varname) {
        $applied_template = str_replace($m[0][$i], sprintf('%s', $vars[$varname]), $applied_template);
      }
    }
    return $applied_template;
  }
}
?>
