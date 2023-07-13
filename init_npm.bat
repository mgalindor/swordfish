npm init --yes && ^
echo 'Initialized NPM project' && ^
npm install  @commitlint/cli @commitlint/config-conventional && ^
echo 'Installed commitlint' && ^
echo module.exports = { extends: ['@commitlint/config-conventional'] }; > commitlint.config.js && ^
echo 'Created .commitlintrc.js file' && ^
npm install husky --save-dev && ^
echo 'Initialized Huskey' && ^
npx husky install && ^
echo 'Configured huskey' && ^
npx husky add .husky/commit-msg  "npx --no -- commitlint --edit ${1}" && ^
echo 'added hook'