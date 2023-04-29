module.exports = {
  root: false,
  env: {
    node: false
  },
  'extends': [
    // 'plugin:vue/essential',
    // 'eslint:recommended',
    // '@vue/typescript/recommended'
  ],
  parserOptions: {
    ecmaVersion: 2020
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    // "@typescript-eslint/explicit-module-boundary-types": ["error"]
    "no-unused-vars": "off",
    // "@typescript-eslint/no-unused-vars": ["error"],
    // "@typescript-eslint/ban-ts-comment": "off",
    // "@typescript-eslint/explicit-function-return-type": "off",
    // "@typescript-eslint/no-explicit-any": ["off"]
  }
}