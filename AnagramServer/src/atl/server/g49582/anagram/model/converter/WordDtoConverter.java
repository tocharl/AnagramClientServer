package atl.server.g49582.anagram.model.converter;

import atl.server.g49582.anagram.dto.WordDto;
import atl.server.g49582.anagram.model.Word;

/**
 * Turns a WordDto object into a Word Object.
 *
 * @author jlc
 */
public class WordDtoConverter implements GenericConverter<WordDto, Word> {

    @Override
    public Word apply(WordDto input) {
        Word output = new Word(input.getText());
        return output;
    }

}
